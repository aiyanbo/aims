package aims.http

import aims.core.RestRes
import aims.model.RequestContext
import aims.routing.RouteActor
import akka.actor.ActorSystem
import akka.http.model.{ HttpResponse, StatusCodes }
import akka.http.server.Directives
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import akka.http.model.headers._
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Failure

/**
 * Component:
 * Description:
 * Date: 15/1/22
 * @author Andy Ai
 */
trait HttpService extends Directives {
  implicit val system: ActorSystem
  implicit val handlers: List[RestRes]
  implicit val materializer = FlowMaterializer()

  val timeout: Timeout
  val forcedContentLengthHeader: Boolean

  import system.dispatcher

  private val router = system.actorOf(RouteActor.props(handlers))

  def route = {
    (get | delete) {
      extractRequestContext { ctx ⇒
        complete(dispatchRequest(ctx, None))
      }
    } ~ (post | patch | put) {
      extractRequestContext { ctx ⇒
        if (forcedContentLengthHeader) {
          optionalHeaderValueByName(`Content-Length`.name) {
            case Some(contentLength) if contentLength.toLong > 0 ⇒
              entity(as[Option[String]]) { payload ⇒
                complete(dispatchRequest(ctx, payload))
              }
            case _ ⇒
              complete(dispatchRequest(ctx, None))
          }
        } else {
          entity(as[Option[String]]) { payload ⇒
            complete(dispatchRequest(ctx, payload))
          }
        }
      }

    }
  }

  private def dispatchRequest(ctx: akka.http.server.RequestContext, payload: Option[String]): Future[HttpResponse] = {
    router.ask(RequestContext(ctx.request, payload))(timeout).collect {
      case response: HttpResponse ⇒ response
      case Failure(exception)     ⇒ HttpResponse(StatusCodes.InternalServerError)
    }
  }

}

class HttpServiceBinding(
  val system: ActorSystem,
  val handlers: List[RestRes],
  val timeout: Timeout = 5.seconds,
  val forcedContentLengthHeader: Boolean = false) extends HttpService
package aims.http

import aims.core.RestRes
import aims.model.RequestContext
import aims.routing.RouteActor
import akka.actor.ActorSystem
import akka.http.model.{ HttpResponse, StatusCodes }
import akka.http.server.Directives
import akka.pattern.ask
import akka.stream.FlowMaterializer

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

  import system.dispatcher

  private val router = system.actorOf(RouteActor.props(handlers))

  def route = {
    (get | delete) {
      extractRequestContext {
        ctx ⇒
          complete {
            router.ask(RequestContext(ctx.request, None))(10.seconds).collect {
              case response: HttpResponse ⇒ response
              case Failure(exception)     ⇒ HttpResponse(StatusCodes.InternalServerError)
            }
          }
      }
    } ~ (post | patch | put) {
      //      optionalHeaderValueByName(HttpHeaders.CONTENT_LENGTH) {
      //        case Some(contentLength) if contentLength.toLong > 0 =>
      extractRequestContext {
        ctx ⇒
          entity(as[Option[String]]) { payload ⇒
            complete {
              router.ask(RequestContext(ctx.request, payload))(10.seconds).collect {
                case response: HttpResponse ⇒ response
                case Failure(exception)     ⇒ HttpResponse(StatusCodes.InternalServerError)
              }
            }
          }
      }
      //        case _ =>
      //          extractRequestContext {
      //            ctx ⇒
      //              Await.result(router.ask(ctx.request)(10.seconds).collect {
      //                case response: HttpResponse ⇒ complete(response)
      //                case Rejected(rejections) => reject(rejections: _*)
      //                case Failure(exception) ⇒ complete(HttpResponse(StatusCodes.InternalServerError))
      //              }, 10.seconds)
      //          }
      //      }

    }
  }

}

class HttpServiceBinding(val system: ActorSystem, val handlers: List[RestRes]) extends HttpService

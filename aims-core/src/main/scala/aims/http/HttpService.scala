package aims.http

import java.io.File

import aims.core.{ RequestContext, Restlet }
import aims.routing.RouteActor
import akka.actor.ActorSystem
import akka.http.model.Multipart.FormData
import akka.http.model.headers._
import akka.http.model.{ HttpResponse, StatusCodes }
import akka.http.server.{ Route, Directives }
import akka.pattern.ask
import akka.stream.ActorFlowMaterializer
import akka.util.Timeout

import scala.concurrent.{ Await, Future }
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
  implicit val handlers: List[Restlet]
  implicit val materializer: ActorFlowMaterializer

  val timeout: Timeout
  val forcedContentLengthHeader: Boolean

  import system.dispatcher

  private val router = system.actorOf(RouteActor.props(handlers))

  def enabledMetrics(route: Route): Route = metricsRoute ~ route

  def remixRoute: Route = queryRoute ~ commandRoute

  def metricsRoute: Route = path("metrics" / Rest) { rest ⇒
    get {
      complete("not support yet!")
    }
  }

  def queryRoute: Route = {
    path("attachments" / Rest) { rest ⇒
      get {
        extractRequestContext { ctx ⇒
          val file: File = Await.result(router.ask(RequestContext(ctx.request, None))(timeout).map(_.asInstanceOf[File]), timeout.duration)
          respondWithHeader(`Content-Disposition`.apply(ContentDispositionTypes.attachment, Map("filename" -> file.getName))) {
            getFromFile(file)
          }
        }
      }
    } ~ get {
      extractRequestContext { ctx ⇒
        complete(dispatchRequest(ctx))
      }
    }
  }

  def commandRoute: Route = {
    delete {
      extractRequestContext { ctx ⇒
        complete(dispatchRequest(ctx))
      }
    } ~ path("attachments" / Rest) { rest ⇒
      post {
        extractRequestContext { ctx ⇒
          entity(as[FormData]) { formData ⇒
            complete(dispatchRequest(ctx, formData = Some(formData)))
          }
        }
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
              complete(dispatchRequest(ctx))
          }
        } else {
          entity(as[Option[String]]) { payload ⇒
            complete(dispatchRequest(ctx, payload))
          }
        }
      }

    }
  }

  private def dispatchRequest(ctx: akka.http.server.RequestContext, payload: Option[String] = None, formData: Option[FormData] = None): Future[HttpResponse] = {
    router.ask(RequestContext(ctx.request, payload, formData))(timeout).collect {
      case response: HttpResponse ⇒ response
      case Failure(exception)     ⇒ HttpResponse(StatusCodes.InternalServerError)
    }
  }

}

class HttpServiceBinding(
  val system: ActorSystem,
  val materializer: ActorFlowMaterializer,
  val handlers: List[Restlet],
  val timeout: Timeout = 5.seconds,
  val forcedContentLengthHeader: Boolean = false) extends HttpService

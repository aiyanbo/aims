package aims.core

import aims.model.HandleResult.{ Complete, Rejected, Success }
import aims.model.{ Event, HandleResult }
import akka.actor.{ Actor, Props }
import akka.http.model.{ HttpResponse, StatusCodes }

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class RestResActor(res: RestRes) extends Actor {

  override def receive: Receive = {
    case event: Event ⇒
      event.responder ! execute(event)
  }

  private def execute(event: Event) = {
    try {
      res.handle.applyOrElse(event, unhandle) match {
        case Complete(response)   ⇒ response
        //TODO: Marshall result
        case Success(result)      ⇒ HttpResponse(entity = result.toString)
        // TODO: transform http response
        case Rejected(rejections) ⇒ rejections
      }
    } catch {
      case e: Throwable ⇒ HttpResponse(StatusCodes.InternalServerError)
    }
  }

  private def unhandle(event: Event): HandleResult = {
    HandleResult.Complete(HttpResponse(StatusCodes.NotImplemented, entity = StatusCodes.NotImplemented.defaultMessage))
  }
}

object RestResActor {
  def props(res: RestRes): Props = {
    Props(new RestResActor(res))
  }
}

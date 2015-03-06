package aims.core

import aims.marshalling.MarshallingActor
import aims.model.HandleResult.{ Complete, Failure, Rejected, Success }
import aims.model.{ Event, HandleResult, Marshalling }
import akka.actor.{ Actor, ActorSelection, Props }
import akka.http.model._

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class RestResActor(res: RestRes) extends Actor {
  // have a better way?
  private lazy val marshaller: ActorSelection = context.actorSelection("/user/" + MarshallingActor.name)

  override def receive: Receive = {
    case event: Event ⇒ marshaller ! Marshalling(execute(event), event.request, event.responder)
  }

  private def execute(event: Event) = {
    try {
      res.handle.applyOrElse(event, unhandle) match {
        case Complete(response)   ⇒ response
        case Success(result)      ⇒ result
        case Rejected(rejections) ⇒ rejections
        case Failure(causes)      ⇒ causes
      }
    } catch {
      case e: Throwable ⇒ e
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

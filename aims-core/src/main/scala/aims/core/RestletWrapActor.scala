package aims.core

import aims.core.RestletResult._
import aims.marshalling.MarshallingActor
import aims.model.{ Event, Marshalling }
import akka.actor.{ Actor, ActorSelection, Props }
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class RestletWrapActor(res: Restlet) extends Actor {
  // have a better way?
  private lazy val marshaller: ActorSelection = context.actorSelection("/user/" + MarshallingActor.name)

  override def receive: Receive = {
    case event: Event ⇒ marshaller ! Marshalling(execute(event), event.request, event.responder)
  }

  private def execute(event: Event) = {
    try {
      res.handle.applyOrElse(event, unhandle) match {
        case FromFile(file)                          ⇒ file
        case Complete(response)                      ⇒ response
        case aims.core.RestletResult.Success(result) ⇒ result
        case Rejected(rejections)                    ⇒ rejections
        case Failure(causes)                         ⇒ causes
      }
    } catch {
      case e: Throwable ⇒ e
    }
  }

  private def unhandle(event: Event): RestletResult = {
    RestletResult.Complete(HttpResponse(NotImplemented, entity = NotImplemented.defaultMessage))
  }

}

object RestletWrapActor {
  def props(res: Restlet): Props = {
    Props(new RestletWrapActor(res))
  }
}

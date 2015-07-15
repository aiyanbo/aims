package aims.routing

import aims.core
import aims.core.{ Restlet, RestletWrapActor }
import aims.marshalling.MarshallingActor
import aims.model.{ Event, Marshalling }
import akka.actor._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives

import scala.collection.mutable

/**
 * Component: INTERNAL API
 * Description:
 * Date: 15/1/20
 * @author Andy Ai
 */
private[aims] class RouteActor(res: List[Restlet]) extends Actor with ActorLogging with Directives {

  private lazy val marshaller: ActorSelection = context.actorSelection("/user/" + MarshallingActor.name)

  private lazy val actorPool: mutable.HashMap[Restlet, ActorRef] = mutable.HashMap[Restlet, ActorRef]()

  override def receive: Receive = {
    case ctx: core.RequestContext ⇒
      val request = ctx.request
      val (path, method) = (request.uri.path, request.method)
      res map { r ⇒
        (r.pattern.apply(path), r)
      } filter {
        r ⇒
          r._1 match {
            case Some(_) ⇒ true
            case None    ⇒ false
          }
      } match {
        case Nil ⇒ marshaller ! Marshalling(HttpResponse(NotFound), request, sender())
        case rs ⇒
          rs.find(r ⇒ r._2.method == method) match {
            case None ⇒ marshaller ! Marshalling(HttpResponse(MethodNotAllowed), request, sender())
            case Some(r) ⇒
              val worker = actorPool.getOrElseUpdate(r._2, context.actorOf(RestletWrapActor.props(r._2)))
              worker ! Event(sender(), r._1.get, ctx.payload, ctx.formData, request)
          }
      }
  }

}

object RouteActor {
  def props(res: List[Restlet]): Props = {
    Props(new RouteActor(res))
  }
}

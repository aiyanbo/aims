package aims.routing

import aims.core.{ RestRes, RestResActor }
import aims.model.{ Event, RequestContext }
import akka.actor.{ ActorRef, Actor, ActorLogging, Props }
import akka.http.model.{ HttpResponse, StatusCodes }
import akka.http.server._

import scala.collection.mutable

/**
 * Component: INTERNAL API
 * Description:
 * Date: 15/1/20
 * @author Andy Ai
 */
private[aims] class RouteActor(res: List[RestRes]) extends Actor with ActorLogging with Directives {

  private lazy val actorPool: mutable.HashMap[RestRes, ActorRef] = mutable.HashMap[RestRes, ActorRef]()

  override def receive: Receive = {
    case ctx: RequestContext ⇒
      val request = ctx.request
      val (path, method) = (request.uri.path, request.method)
      res map { r ⇒
        (r.pattern().apply(path), r)
      } filter {
        r ⇒
          r._1 match {
            case Some(_) ⇒ true
            case None    ⇒ false
          }
      } match {
        case Nil ⇒ sender() ! HttpResponse(StatusCodes.NotFound)
        case rs ⇒
          rs.find(r ⇒ r._2.method() == method) match {
            case None ⇒ sender() ! HttpResponse(StatusCodes.MethodNotAllowed)
            case Some(r) ⇒
              val worker = actorPool.getOrElseUpdate(r._2, context.actorOf(RestResActor.props(r._2)))
              worker ! Event(sender(), r._1.get, ctx.payload, request)
          }
      }
  }

}

object RouteActor {
  def props(res: List[RestRes]): Props = {
    Props(new RouteActor(res))
  }
}

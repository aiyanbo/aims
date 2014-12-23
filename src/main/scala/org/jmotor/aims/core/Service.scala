package org.jmotor.aims.core

import akka.actor.{ Actor, ActorLogging, Props }
import akka.http.model.HttpResponse
import akka.http.model.StatusCodes._
import org.jmotor.aims.core.Resources.Resource

import scala.runtime.BoxedUnit

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
object Service {
  type Handler = PartialFunction[Any, Any]

}

trait Service {
  def pattern(): String

  def resource(): Resource = Resource(pattern(), handler = handler)

  def handler: Service.Handler

}

trait OperationService extends Service {
  def pattern(): String
}

class MicroService(handler: Service.Handler) extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case request: ServiceRequest ⇒
      log.debug(s"handle request: ${request.request.uri.path}")
      handler.apply(request) match {
        case unit: BoxedUnit        ⇒ request.original ! HttpResponse(OK)
        case response: HttpResponse ⇒ request.original ! response
        case string: String         ⇒ request.original ! HttpResponse(OK, entity = string)
        case entity                 ⇒ request.original ! HttpResponse(OK, entity = entity.toString)
      }
  }
}

object MicroService {
  def props(handler: Service.Handler): Props = {
    Props(new MicroService(handler))
  }
}

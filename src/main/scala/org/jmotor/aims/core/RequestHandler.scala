package org.jmotor.aims.core

import akka.actor.{Actor, ActorLogging}
import akka.http.model.{HttpRequest, HttpResponse}

import scala.runtime.BoxedUnit

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
abstract class RequestHandler extends Actor with ActorLogging {
  override def receive: Receive = {
    case request: ServiceRequest =>
      handle(request.request) match {
        case unit: BoxedUnit =>
          request.original ! HttpResponse(200)
        case response: HttpResponse =>
          request.original ! response
        case string: String =>
          request.original ! HttpResponse(200, entity = string)
        case entity =>
          request.original ! HttpResponse(200, entity = entity.toString)
      }
    case default => log.warning(s"Unsupported request: ${default.getClass}")
  }

  def handle(request: HttpRequest): Any
}

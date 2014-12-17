package org.jmotor.aims.core

import akka.actor.{Actor, ActorLogging}
import akka.http.model.{HttpRequest, HttpResponse}

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
abstract class RequestHandler extends Actor with ActorLogging {
  override def receive: Receive = {
    case request: ServiceRequest =>
      val res = handle(request.request)
      request.original ! HttpResponse(200, entity = res.toString)
    case default => log.warning(s"Unsupported request: ${default.getClass}")
  }

  def handle(request: HttpRequest): Any
}

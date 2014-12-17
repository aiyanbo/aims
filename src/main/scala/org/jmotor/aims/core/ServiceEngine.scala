package org.jmotor.aims.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.model.{HttpRequest, HttpResponse}

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class ServiceEngine(services: Map[String, ServiceApi]) extends Actor with ActorLogging {
  private val actors = scala.collection.mutable.HashMap[String, ActorRef]()

  override def receive: Receive = {
    case request: HttpRequest =>
      services.get(request.uri.path.toString()) match {
        case Some(service) =>
          actors.getOrElseUpdate(service.pattern, context.actorOf(service.props)) ! ServiceRequest(sender(), request)
        case None =>
          sender() ! HttpResponse(404)
      }
    case default => log.warning(s"Unsupported request: ${default.getClass}")
  }
}

object ServiceEngine {
  def props(services: Map[String, ServiceApi]): Props = {
    Props(new ServiceEngine(services))
  }
}
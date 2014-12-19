package org.jmotor.aims.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.model.{HttpRequest, HttpResponse}

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class ServiceEngine(services: Map[String, InternalServiceApi]) extends Actor with ActorLogging {
  private val actors = scala.collection.mutable.HashMap[String, ActorRef]()

  override def receive: Receive = {
    case request: HttpRequest =>
      val path = request.method.name + "::" + request.uri.path.toString
      val res = services.filterKeys(pattern => path.matches(pattern))
      if (res.isEmpty) {
        sender() ! HttpResponse(404)
      } else {
        val service = res.values.headOption.get
        val parameters = path.split("/")
        actors.getOrElseUpdate(service.pattern, context.actorOf(service.props)) ! ServiceRequest(sender(), request, service.parameters.mapValues(parameters(_)))
      }
    case default => log.warning(s"Unsupported request: ${default.getClass}")
  }
}

object ServiceEngine {
  def props(services: Map[String, InternalServiceApi]): Props = {
    Props(new ServiceEngine(services))
  }
}

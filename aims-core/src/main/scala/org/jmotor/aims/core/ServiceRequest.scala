package org.jmotor.aims.core

import akka.actor.ActorRef
import akka.http.model.HttpRequest

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
case class ServiceRequest(original: ActorRef, request: HttpRequest, pathParameters: Map[String, String])

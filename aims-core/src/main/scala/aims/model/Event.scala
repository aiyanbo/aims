package aims.model

import akka.actor.ActorRef
import akka.http.model.HttpRequest

/**
 * Component:
 * Description:
 * Date: 15/1/29
 * @author Andy Ai
 */
case class Event(responder: ActorRef, extractions: Any, payload: Option[String], request: HttpRequest)

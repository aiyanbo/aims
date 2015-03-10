package aims.model

import akka.actor.ActorRef
import akka.http.model.HttpRequest
import akka.http.model.Multipart.FormData

/**
 * Component:
 * Description:
 * Date: 15/1/29
 * @author Andy Ai
 */
case class Event(responder: ActorRef, extractions: Any, payload: Option[String], formData: Option[FormData], request: HttpRequest)

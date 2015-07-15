package aims.model

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.Multipart.FormData

/**
 * Component:
 * Description:
 * Date: 15/1/29
 * @author Andy Ai
 */
case class Event(responder: ActorRef, extractions: Any, payload: Option[String], formData: Option[FormData], request: HttpRequest)

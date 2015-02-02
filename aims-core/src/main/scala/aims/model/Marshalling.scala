package aims.model

import akka.actor.ActorRef
import akka.http.model.HttpRequest

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
case class Marshalling(marshallable: Any, request: HttpRequest, responder: ActorRef)

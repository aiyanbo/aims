package aims.core

import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.model.{ HttpMethod, MediaType, MediaTypes }

/**
 * Component:
 * Description:
 * Date: 15/1/22
 * @author Andy Ai
 */
trait Restlet {
  type Handle = Restlet.Handler

  val method: HttpMethod

  val pattern: PatternMatcher

  val acceptMediaTypes: List[MediaType] = List(MediaTypes.`application/json`)

  def handle: Handle

}

object Restlet {

  type Handler = PartialFunction[Event, RestletResult]

}

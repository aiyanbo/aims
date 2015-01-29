package aims.core

import aims.model.{ Event, HandleResult }
import aims.routing.PatternMatcher
import akka.http.model.{ HttpMethod, MediaType, MediaTypes }

/**
 * Component:
 * Description:
 * Date: 15/1/22
 * @author Andy Ai
 */
trait RestRes {
  type Handle = RestRes.Handler

  type Result = RestRes.Result

  def method(): HttpMethod

  def pattern(): PatternMatcher

  def acceptMediaTypes(): List[MediaType] = List(MediaTypes.`application/json`)

  def handle: Handle

}

object RestRes {
  type Result = HandleResult

  type Handler = PartialFunction[Event, Result]
}

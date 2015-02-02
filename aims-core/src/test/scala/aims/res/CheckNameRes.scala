package aims.res

import aims.core.RestRes
import aims.model.Event
import aims.model.HandleResult._
import aims.routing.PatternMatcher
import aims.routing.Patterns._
import akka.http.model.{ HttpMethod, HttpMethods, HttpResponse, StatusCodes }

/**
 * Component:
 * Description:
 * Date: 15/1/29
 * @author Andy Ai
 */
class CheckNameRes extends RestRes {
  override def method(): HttpMethod = HttpMethods.POST

  override def pattern(): PatternMatcher = PatternMatcher(ph("check_name"))

  override def handle: Handle = {
    case event: Event ⇒
      event.payload match {
        case Some(name) if name.length < 5 ⇒ HttpResponse(StatusCodes.BadRequest, entity = "Name's length must be then 5")
        case Some(name)                    ⇒ HttpResponse(StatusCodes.OK, entity = s"$name is available")
        case None                          ⇒ HttpResponse(StatusCodes.BadRequest, entity = "Name cannot be empty")
      }
  }
}

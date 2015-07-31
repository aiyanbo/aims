package aims.res

import aims.core.Restlet
import aims.core.RestletResult._
import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.scaladsl.model.{HttpMethod, HttpMethods, HttpResponse, StatusCodes}

/**
 * Component:
 * Description:
 * Date: 15/1/29
 * @author Andy Ai
 */
class CheckNameRes extends Restlet {
  override val method: HttpMethod = HttpMethods.POST

  override val pattern: PatternMatcher = PatternMatcher("/check_name")

  override def handle: Handle = {
    case event: Event ⇒
      event.payload match {
        case Some(name) if name.length < 5 ⇒ HttpResponse(StatusCodes.BadRequest, entity = "Name's length must be then 5")
        case Some(name)                    ⇒ HttpResponse(StatusCodes.OK, entity = s"$name is available")
        case None                          ⇒ HttpResponse(StatusCodes.BadRequest, entity = "Name cannot be empty")
      }
  }
}

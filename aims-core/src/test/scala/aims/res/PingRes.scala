package aims.res

import aims.core.{ Restlet, RestletResult }
import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.model.{ HttpMethod, HttpMethods, HttpResponse }
import akka.http.scaladsl.model.{ HttpResponse, HttpMethods, HttpMethod }

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class PingRes extends Restlet {

  override val method: HttpMethod = HttpMethods.GET

  override val pattern: PatternMatcher = PatternMatcher("/ping")

  override def handle: Handle = {

    case event: Event ⇒
      event.request.uri.query.get("e") match {
        case Some(v) ⇒ throw new IllegalArgumentException
        case None ⇒
          RestletResult.Complete(HttpResponse(entity = "ping"))
      }
  }

}

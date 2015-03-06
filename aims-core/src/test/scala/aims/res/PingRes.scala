package aims.res

import aims.core.RestRes
import aims.model.{ Event, HandleResult }
import aims.routing.PatternMatcher
import aims.routing.Patterns._
import akka.http.model.{ HttpMethod, HttpMethods, HttpResponse }

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class PingRes extends RestRes {

  override val method: HttpMethod = HttpMethods.GET

  override val pattern: PatternMatcher = PatternMatcher("/ping")

  override def handle: Handle = {

    case event: Event ⇒
      event.request.uri.query.get("e") match {
        case Some(v) ⇒ throw new IllegalArgumentException
        case None ⇒
          HandleResult.Complete(HttpResponse(entity = "ping"))
      }
  }

}

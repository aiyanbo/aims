package aims.http

import java.io.File

import aims.core.{ RestletResult, Restlet }
import aims.model.Event
import aims.routing.PatternMatcher
import aims.routing.PatternMatcher
import akka.http.model.HttpMethod
import akka.http.model.HttpMethods._

/**
 * Component:
 * Description:
 * Date: 15/3/10
 * @author Andy Ai
 */
trait AttachmentsDownloadRestlet extends Restlet {
  val root: PatternMatcher = "/attachments"

  override val method: HttpMethod = GET

  override def handle: Handle = {
    case event: Event ⇒ fromFile(event)
  }

  def fromFile(event: Event): File
}

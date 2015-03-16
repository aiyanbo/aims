package aims.http

import aims.core.{ RestletResult, Restlet }
import aims.model.Event
import aims.routing.PatternMatcher
import aims.routing.PatternMatcher._
import akka.http.model.HttpMethod
import akka.http.model.HttpMethods._

/**
 * Component:
 * Description:
 * Date: 15/3/10
 * @author Andy Ai
 */
trait AttachmentsUploadRestlet extends Restlet {

  override val method: HttpMethod = POST

  override val pattern: PatternMatcher = "/attachments" / attachmentsPattern()

  override def handle: Handle = {
    case event: Event ⇒ saveAttachments(event)
  }

  def attachmentsPattern(): PatternMatcher

  def saveAttachments(event: Event): RestletResult
}

package aims.http

import java.io.File

import aims.core.Restlet
import aims.model.Event
import aims.routing.PatternMatcher
import aims.routing.PatternMatcher._
import akka.http.scaladsl.model.HttpMethod
import akka.http.scaladsl.model.HttpMethods._

/**
 * Component:
 * Description:
 * Date: 15/3/10
 * @author Andy Ai
 */
trait AttachmentsDownloadRestlet extends Restlet {

  override val method: HttpMethod = GET

  override val pattern: PatternMatcher = "/attachments" / attachmentsPattern()

  override def handle: Handle = {
    case event: Event ⇒ fromFile(event)
  }

  def attachmentsPattern(): PatternMatcher

  def fromFile(event: Event): File
}

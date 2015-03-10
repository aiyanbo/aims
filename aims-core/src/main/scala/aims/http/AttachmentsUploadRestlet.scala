package aims.http

import aims.core.Restlet
import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.model.HttpMethod
import akka.http.model.HttpMethods._
import akka.stream.ActorFlowMaterializer

/**
 * Component:
 * Description:
 * Date: 15/3/10
 * @author Andy Ai
 */
trait AttachmentsUploadRestlet extends Restlet {

  implicit val materializer: ActorFlowMaterializer

  val root: PatternMatcher = "/attachments"

  override val method: HttpMethod = POST

  override def handle: Handle = {
    case event: Event ⇒ saveAttachments(event)
  }

  def saveAttachments(event: Event): Unit
}

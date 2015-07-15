package aims.core

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.Multipart.FormData

/**
 * Component:
 * Description:
 * Date: 15/1/26
 * @author Andy Ai
 */
case class RequestContext(request: HttpRequest, payload: Option[String], formData: Option[FormData] = None)

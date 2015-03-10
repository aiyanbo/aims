package aims.core

import akka.http.model.HttpRequest
import akka.http.model.Multipart.FormData

/**
 * Component:
 * Description:
 * Date: 15/1/26
 * @author Andy Ai
 */
case class RequestContext(request: HttpRequest, payload: Option[String], formData: Option[FormData] = None)

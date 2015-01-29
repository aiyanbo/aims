package aims.model

import akka.http.model.HttpRequest

/**
 * Component:
 * Description:
 * Date: 15/1/26
 * @author Andy Ai
 */
case class RequestContext(request: HttpRequest, payload: Option[String])

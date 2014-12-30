package aims.core

import akka.actor.Props
import akka.http.model.{ HttpMethod, HttpMethods, MediaType, MediaTypes }

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
object Resources {

  case class Resource(
    pattern: String, method: HttpMethod = HttpMethods.GET, handler: Service.Handler,
    consumerTypes: List[MediaType] = List(MediaTypes.`application/json`),
    producerTypes: List[MediaType] = List(MediaTypes.`application/json`))

  private[aims] case class ResourceMirror(
    pattern: String, matcher: String, method: HttpMethod,
    props: Props, parameters: Map[String, Int],
    consumerTypes: List[MediaType] = List(MediaTypes.`application/json`),
    producerTypes: List[MediaType] = List(MediaTypes.`application/json`))

}

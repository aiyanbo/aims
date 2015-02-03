package aims.samples.services

import java.util.UUID

import aims.core.RestRes
import akka.http.model.{HttpMethod, HttpMethods}

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class UUIDService extends RestRes {

  override def pattern(): String = "/uuid/gen"

  override def method(): HttpMethod = HttpMethods.GET

  override def handle: Handle = {
    case _ => UUID.randomUUID().toString
  }
}

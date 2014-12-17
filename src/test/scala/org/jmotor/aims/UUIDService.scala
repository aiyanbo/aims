package org.jmotor.aims

import java.util.UUID

import akka.http.model.HttpRequest
import org.jmotor.aims.core.RequestHandler

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class UUIDService extends RequestHandler {
  override def handle(request: HttpRequest): Any = {
    UUID.randomUUID().toString
  }
}

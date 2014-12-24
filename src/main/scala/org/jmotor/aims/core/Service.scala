package org.jmotor.aims.core

import akka.actor.{ Actor, ActorLogging, Props }
import akka.http.model.HttpMethods._
import akka.http.model.MediaTypes._
import akka.http.model.StatusCodes._
import akka.http.model._
import akka.http.model.japi.HttpEntityStrict
import org.jmotor.aims.core.Resources.Resource
import org.jmotor.aims.json.Jackson

import scala.runtime.BoxedUnit

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
object Service {
  type Handler = PartialFunction[Any, Any]

}

trait Service {
  def pattern(): String

  def resource(): Resource = Resource(pattern(), handler = handler)

  def handler: Service.Handler

}

trait OperationService {
  def patterns(): (String, String)

  val pattern = patterns()

  def resources(): List[Resource] = List(
    Resource(pattern._1, POST, handler),
    Resource(pattern._1 + pattern._2, GET, handler),
    Resource(pattern._1 + pattern._2, PUT, handler),
    Resource(pattern._1 + pattern._2, DELETE, handler)
  )

  def handler: Service.Handler = {
    case request: ServiceRequest ⇒
      request.request.method match {
        case GET    ⇒ get(request.pathParameters)
        case PUT    ⇒ update(request.pathParameters, request.request.entity.asInstanceOf[HttpEntityStrict].data().utf8String)
        case POST   ⇒ insert(request.pathParameters, request.request.entity.asInstanceOf[HttpEntityStrict].data().utf8String)
        case DELETE ⇒ delete(request.pathParameters)
        case _      ⇒ HttpResponse(StatusCodes.MethodNotAllowed)
      }
  }

  def get(pathParameters: Map[String, String]): Any

  def insert(pathParameters: Map[String, String], entity: Any): Any

  def update(pathParameters: Map[String, String], entity: Any): Unit

  def delete(pathParameters: Map[String, String]): Unit
}

class MicroService(handler: Service.Handler) extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case request: ServiceRequest ⇒
      log.debug(s"handle request: ${request.request.uri.path}")
      handler.apply(request) match {
        case unit: BoxedUnit        ⇒ request.original ! HttpResponse(OK)
        case response: HttpResponse ⇒ request.original ! response
        case string: String         ⇒ request.original ! HttpResponse(OK, entity = HttpEntity(`text/plain`, string))
        case entity                 ⇒ request.original ! HttpResponse(OK, entity = HttpEntity(`application/json`, Jackson.mapper.writeValueAsString(entity)))
      }
  }
}

object MicroService {
  def props(handler: Service.Handler): Props = {
    Props(new MicroService(handler))
  }
}

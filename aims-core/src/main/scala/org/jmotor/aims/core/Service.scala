package org.jmotor.aims.core

import akka.actor.{ Actor, ActorLogging, Props }
import akka.http.model.HttpMethods._
import akka.http.model.MediaTypes._
import akka.http.model.StatusCodes._
import akka.http.model.Uri.Query
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

trait OperationService[E] {
  def resourceType(): Class[E]

  def patterns(): (String, String)

  val pattern = patterns()

  def resources(): List[Resource] = List(
    Resource(pattern._1, POST, handler),
    Resource(pattern._1, GET, handler),
    Resource(pattern._1 + pattern._2, GET, handler),
    Resource(pattern._1 + pattern._2, PUT, handler),
    Resource(pattern._1 + pattern._2, DELETE, handler)
  )

  def handler: Service.Handler = {
    case request: ServiceRequest ⇒
      request.request.method match {
        case GET if request.pattern == pattern._1 ⇒
          list(request.pathParameters, request.request.uri.query)
        case GET ⇒ get(request.pathParameters, request.request.uri.query) match {
          case Some(e) ⇒ e
          case None    ⇒ HttpResponse(StatusCodes.NotFound)
        }
        case PUT    ⇒ update(request.pathParameters, parseJsonEntity(request))
        case POST   ⇒ insert(request.pathParameters, parseJsonEntity(request))
        case DELETE ⇒ delete(request.pathParameters)
        case _      ⇒ HttpResponse(StatusCodes.MethodNotAllowed)
      }
  }

  private def parseJsonEntity(req: ServiceRequest): E = {
    Jackson.mapper.readValue(req.request.entity.asInstanceOf[HttpEntityStrict].data().utf8String, resourceType()).asInstanceOf[E]
  }

  def get(pathParameters: Map[String, String], query: Query): Option[E]

  def insert(pathParameters: Map[String, String], entity: E): Option[E]

  def update(pathParameters: Map[String, String], entity: E): Unit

  def delete(pathParameters: Map[String, String]): Unit

  def list(pathParameters: Map[String, String], query: Query): List[E]
}

class MicroService(handler: Service.Handler) extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case request: ServiceRequest ⇒
      log.debug(s"handle request: ${request.request.uri.path}")
      handler.apply(request) match {
        case unit: BoxedUnit        ⇒ request.original ! HttpResponse(OK)
        case response: HttpResponse ⇒ request.original ! response
        case string: String         ⇒ request.original ! HttpResponse(OK, entity = HttpEntity(`text/plain`.withParams(Map("charset" -> "UTF-8")), string))
        case number: Number         ⇒ request.original ! HttpResponse(OK, entity = HttpEntity(`text/plain`.withParams(Map("charset" -> "UTF-8")), number.toString))
        case entity                 ⇒ request.original ! HttpResponse(OK, entity = HttpEntity(`application/json`.withParams(Map("charset" -> "UTF-8")), Jackson.mapper.writeValueAsString(entity)))
      }
  }

}

object MicroService {
  def props(handler: Service.Handler): Props = {
    Props(new MicroService(handler))
  }
}

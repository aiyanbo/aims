package aims.marshalling

import java.io.File

import aims.core.Pagination
import aims.core.model.headers.XTotalCount
import aims.json.Jackson
import aims.model.Marshalling
import akka.actor.{ Actor, ActorLogging }
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{ HttpEntity, HttpRequest, HttpResponse }
import akka.http.scaladsl.model.ContentTypes._

import scala.collection.immutable
import scala.runtime.BoxedUnit

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
class MarshallingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case Marshalling(response: HttpResponse, _, responder)          ⇒ responder ! response
    case Marshalling(file: File, _, responder)                      ⇒ responder ! file
    case Marshalling(unit: BoxedUnit, _, responder)                 ⇒ responder ! HttpResponse(OK)
    case Marshalling(str: String, _, responder)                     ⇒ responder ! HttpResponse(OK, entity = str)
    case Marshalling(number: Number, _, responder)                  ⇒ responder ! HttpResponse(OK, entity = number.toString)
    case Marshalling(pagination: Pagination[_], request, responder) ⇒ responder ! marshalPagination(pagination, request)
    case Marshalling(throwable: Throwable, request, responder)      ⇒ responder ! marshalThrowable(throwable)
    case Marshalling(entity, _, responder)                          ⇒ responder ! marshalEntity(entity)
  }

  private def marshalEntity(entity: Any): HttpResponse = {
    HttpResponse(OK, entity = HttpEntity(`application/json`, Jackson.mapper.writeValueAsString(entity)))
  }

  private def marshalThrowable(throwable: Throwable) = {
    val message = throwable.getMessage match {
      case m: String ⇒ m
      case _         ⇒ throwable.toString
    }
    log.error(throwable, message)
    throwable match {
      case runtime: RuntimeException ⇒ HttpResponse(BadRequest, entity = message)
      case err                       ⇒ HttpResponse(InternalServerError, entity = message)
    }
  }

  private def marshalPagination(pagination: Pagination[_], request: HttpRequest): HttpResponse = {
    if (pagination.items.isEmpty) {
      HttpResponse(entity = HttpEntity(`application/json`, "[]"))
    } else {
      pagination.links.filter {
        case LinkParams.next  ⇒ pagination.page < pagination.totalPage
        case LinkParams.prev  ⇒ pagination.page - 1 > 1
        case LinkParams.first ⇒ pagination.page > 1
        case LinkParams.last  ⇒ pagination.page < pagination.totalPage
      }.map {
        case LinkParams.next  ⇒ LinkValue(request.uri.withQuery(request.uri.query.+:("page", (pagination.page + 1).toString)), LinkParams.next)
        case LinkParams.prev  ⇒ LinkValue(request.uri.withQuery(request.uri.query.+:("page", (pagination.page - 1).toString)), LinkParams.prev)
        case LinkParams.first ⇒ LinkValue(request.uri.withQuery(request.uri.query.+:("page", 1.toString)), LinkParams.first)
        case LinkParams.last  ⇒ LinkValue(request.uri.withQuery(request.uri.query.+:("page", pagination.totalPage.toString)), LinkParams.last)
      } match {
        case Nil   ⇒ HttpResponse(status = OK, headers = immutable.Seq(XTotalCount(pagination.totalCount)), entity = HttpEntity(`application/json`, Jackson.mapper.writeValueAsString(pagination.items)))
        case links ⇒ HttpResponse(status = OK, headers = immutable.Seq(Link(links: _*), XTotalCount(pagination.totalCount)), entity = HttpEntity(`application/json`, Jackson.mapper.writeValueAsString(pagination.items)))
      }
    }
  }
}

object MarshallingActor {
  val name = "marshaller"
}

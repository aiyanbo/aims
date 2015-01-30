package aims.core

import aims.core.model.headers.XTotalCount
import aims.json.Jackson
import aims.model.HandleResult.{ Failure, Complete, Rejected, Success }
import aims.model.{ Event, HandleResult }
import akka.actor.{ Actor, Props }
import akka.http.model.MediaTypes._
import akka.http.model.StatusCodes._
import akka.http.model.headers.{ Link, LinkValue, LinkParams }
import akka.http.model._

import scala.collection.immutable
import scala.runtime.BoxedUnit

/**
 * Component:
 * Description:
 * Date: 15/1/27
 * @author Andy Ai
 */
class RestResActor(res: RestRes) extends Actor {

  override def receive: Receive = {
    case event: Event ⇒
      event.responder ! execute(event)
  }

  private def execute(event: Event) = {
    try {
      res.handle.applyOrElse(event, unhandle) match {
        case Complete(response) ⇒ response
        //TODO: Marshall result
        case Success(result) ⇒ result match {
          case unit: BoxedUnit           ⇒ HttpResponse(StatusCodes.OK)
          case str: String               ⇒ HttpResponse(entity = str)
          case pagination: Pagination[_] ⇒ makePagination(pagination, event.request)
        }
        // TODO: transform http response
        case Rejected(rejections) ⇒ rejections
        case Failure(causes)      ⇒ HttpResponse(StatusCodes.InternalServerError)
      }
    } catch {
      case e: Throwable ⇒ HttpResponse(StatusCodes.InternalServerError)
    }
  }

  private def unhandle(event: Event): HandleResult = {
    HandleResult.Complete(HttpResponse(StatusCodes.NotImplemented, entity = StatusCodes.NotImplemented.defaultMessage))
  }

  private def makePagination(pagination: Pagination[_], request: HttpRequest): HttpResponse = {
    if (pagination.items.isEmpty) {
      return HttpResponse(OK, entity = HttpEntity(ContentTypes.`application/json`, "[]"))
    }
    pagination.links.filter {
      case LinkParams.next  ⇒ pagination.page < pagination.totalPage
      case LinkParams.prev  ⇒ pagination.page - 1 > 1
      case LinkParams.first ⇒ pagination.page > 1
      case LinkParams.last  ⇒ pagination.page < pagination.totalPage
    }.map {
      case LinkParams.next  ⇒ LinkValue(request.uri.withQuery(request.uri.query.toMap + ("page" -> (pagination.page + 1).toString)), LinkParams.next)
      case LinkParams.prev  ⇒ LinkValue(request.uri.withQuery(request.uri.query.toMap + ("page" -> (pagination.page - 1).toString)), LinkParams.prev)
      case LinkParams.first ⇒ LinkValue(request.uri.withQuery(request.uri.query.toMap + ("page" -> 1.toString)), LinkParams.first)
      case LinkParams.last  ⇒ LinkValue(request.uri.withQuery(request.uri.query.toMap + ("page" -> pagination.totalPage.toString)), LinkParams.last)
    } match {
      case Nil   ⇒ HttpResponse(status = OK, headers = immutable.Seq(XTotalCount(pagination.totalCount)), entity = HttpEntity(ContentTypes.`application/json`, Jackson.mapper.writeValueAsString(pagination.items)))
      case links ⇒ HttpResponse(status = OK, headers = immutable.Seq(Link(links: _*), XTotalCount(pagination.totalCount)), entity = HttpEntity(ContentTypes.`application/json`, Jackson.mapper.writeValueAsString(pagination.items)))
    }

  }
}

object RestResActor {
  def props(res: RestRes): Props = {
    Props(new RestResActor(res))
  }
}

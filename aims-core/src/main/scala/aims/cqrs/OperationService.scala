package aims.cqrs

import aims.core.RestRes
import aims.model.Event
import aims.model.HandleResult._
import aims.routing.PatternMatcher
import aims.util.Tuples
import akka.http.model.HttpMethods._
import akka.http.model.StatusCodes._
import akka.http.model.{ HttpMethod, HttpResponse }

/**
 * Component:
 * Description:
 * Date: 15/2/3
 * @author Andy Ai
 */
abstract class OperationService[E] extends CommandService with QueryService[E] {
  def basicPattern(): PatternMatcher

  def identityPattern(): PatternMatcher

  def identity(event: Event): Any

  def resources(): List[RestRes] = List(
    InsertOperation, DeleteOperation, UpdateOperation, ModifyOperation, FindOperation, ListOperation)

  object InsertOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern()

    override val method: HttpMethod = POST

    override def handle: Handle = {
      case event: Event ⇒
        insert(event)
    }
  }

  object DeleteOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern() / identityPattern()

    override val method: HttpMethod = DELETE

    override def handle: Handle = {
      case event: Event ⇒
        delete(event)
    }
  }

  object UpdateOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern() / identityPattern()

    override val method: HttpMethod = PUT

    override def handle: Handle = {
      case event: Event ⇒
        if (Tuples.tail(event.extractions.asInstanceOf[Product]) == identity(event)) {
          update(event)
        } else {
          new IllegalArgumentException("Identity not matched")
        }
    }
  }

  object ModifyOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern() / identityPattern()

    override val method: HttpMethod = PATCH

    override def handle: Handle = {
      case event: Event ⇒
        if (Tuples.tail(event.extractions.asInstanceOf[Product]) == identity(event)) {
          modify(event)
        } else {
          new IllegalArgumentException("Identity not matched")
        }
    }
  }

  object FindOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern() / identityPattern()

    override val method: HttpMethod = GET

    override def handle: Handle = {
      case event: Event ⇒ get(event) match {
        case Some(dto) ⇒ dto
        case None      ⇒ HttpResponse(NotFound)
      }
    }
  }

  object ListOperation extends RestRes {
    override val pattern: PatternMatcher = basicPattern()

    override val method: HttpMethod = GET

    override def handle: Handle = {
      case event: Event ⇒ pagination(event)
    }
  }

}

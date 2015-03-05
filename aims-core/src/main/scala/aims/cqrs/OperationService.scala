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

  def resources(): List[RestRes] = {
    List(
      new RestRes {
        override def pattern(): PatternMatcher = identityPattern()

        override def method(): HttpMethod = GET

        override def handle: Handle = {
          case event: Event ⇒ get(event) match {
            case Some(dto) ⇒ dto
            case None      ⇒ HttpResponse(NotFound)
          }
        }
      },
      new RestRes {
        override def pattern(): PatternMatcher = basicPattern()

        override def method(): HttpMethod = GET

        override def handle: Handle = {
          case event: Event ⇒ pagination(event)
        }
      },
      new RestRes {
        override def pattern(): PatternMatcher = identityPattern()

        override def method(): HttpMethod = PATCH

        override def handle: Handle = {
          case event: Event ⇒
            if (Tuples.tail(event.extractions) == identity(event)) {
              modify(event)
            } else {
              new IllegalArgumentException("Identity not matched")
            }
        }
      },
      new RestRes {
        override def pattern(): PatternMatcher = identityPattern()

        override def method(): HttpMethod = PUT

        override def handle: Handle = {
          case event: Event ⇒
            if (Tuples.tail(event.extractions) == identity(event)) {
              update(event)
            } else {
              new IllegalArgumentException("Identity not matched")
            }
        }
      },
      new RestRes {
        override def pattern(): PatternMatcher = identityPattern()

        override def method(): HttpMethod = DELETE

        override def handle: Handle = {
          case event: Event ⇒
            delete(event)
        }
      },
      new RestRes {
        override def pattern(): PatternMatcher = basicPattern()

        override def method(): HttpMethod = POST

        override def handle: Handle = {
          case event: Event ⇒
            insert(event)
        }
      })
  }
}

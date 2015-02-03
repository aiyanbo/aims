package aims.cqrs

import aims.core.Pagination
import aims.model.Event

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
trait QueryService[E] {
  def get(event: Event): Option[E]

  def pagination(event: Event): Pagination[E]
}

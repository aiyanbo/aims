package aims.cqrs

import aims.model.Event

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
trait CommandService {
  def insert(event: Event): Any

  def update(event: Event): Unit

  def modify(event: Event): Unit

  def delete(event: Event): Unit
}

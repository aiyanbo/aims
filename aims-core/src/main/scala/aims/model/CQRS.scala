package aims.model

/**
 * Component:
 * Description:
 * Date: 15/2/3
 * @author Andy Ai
 */
object CQRS extends Enumeration {
  type CQRS = Value

  val QUERY, COMMAND, REMIX = Value
}

package aims.cqrs

/**
 * Component:
 * Description:
 * Date: 15/2/3
 * @author Andy Ai
 */
trait OperationService[E] extends CommandService with QueryService[E]

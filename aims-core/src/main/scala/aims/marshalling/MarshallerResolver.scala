package aims.marshalling

/**
 * Component:
 * Description:
 * Date: 15/2/2
 * @author Andy Ai
 */
trait MarshallerResolver {
  def marshal(marshallable: Any): String

  def unmarshal(payload: String): Any
}

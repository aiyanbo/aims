package aims.util

/**
 * Component:
 * Description:
 * Date: 15/2/3
 * @author Andy Ai
 */
object Tuples {
  def tail(tuple: Product): Any = {
    tuple.productElement(tuple.productArity - 1)
  }
}

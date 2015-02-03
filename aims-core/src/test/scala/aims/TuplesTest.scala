package aims

import aims.util.Tuples
import org.scalatest.FunSuite

/**
 * Component:
 * Description:
 * Date: 15/2/3
 * @author Andy Ai
 */
class TuplesTest extends FunSuite {
  test("Get tuple tail element") {
    assertResult(3)(Tuples.tail(Tuple3(1, 2, 3)))
  }
}

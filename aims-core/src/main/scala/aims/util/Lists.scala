package aims.util

import scala.language.implicitConversions

/**
 * Component:
 * Description:
 * Date: 15/1/23
 * @author Andy Ai
 */
object Lists {
  implicit def toTuple(list: List[_]): Product with Serializable = list match {
    case List(a)                            ⇒ Tuple1(a)
    case List(a, b)                         ⇒ Tuple2(a, b)
    case List(a, b, c)                      ⇒ Tuple3(a, b, c)
    case List(a, b, c, d)                   ⇒ Tuple4(a, b, c, d)
    case List(a, b, c, d, e)                ⇒ Tuple5(a, b, c, d, e)
    case List(a, b, c, d, e, f)             ⇒ Tuple6(a, b, c, d, e, f)
    case List(a, b, c, d, e, f, g)          ⇒ Tuple7(a, b, c, d, e, f, g)
    case List(a, b, c, d, e, f, g, h)       ⇒ Tuple8(a, b, c, d, e, f, g, h)
    case List(a, b, c, d, e, f, g, h, i)    ⇒ Tuple9(a, b, c, d, e, f, g, h, i)
    case List(a, b, c, d, e, f, g, h, i, j) ⇒ Tuple10(a, b, c, d, e, f, g, h, i, j)
  }
}

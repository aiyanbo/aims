package aims.core.model.headers

import akka.http.scaladsl.model.headers.CustomHeader

/**
 * Component:
 * Description:
 * Date: 2015/1/4
 * @author Andy Ai
 */
class XTotalCount(totalCount: Int) extends CustomHeader {
  override def name(): String = "X-Total-Count"

  override def value(): String = totalCount.toString
}

object XTotalCount {
  def apply(totalCount: Int): XTotalCount = {
    new XTotalCount(totalCount)
  }
}

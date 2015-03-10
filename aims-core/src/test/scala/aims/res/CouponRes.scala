package aims.res

import aims.core.Restlet
import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.model.{ HttpMethod, HttpMethods }

/**
 * Component:
 * Description:
 * Date: 15/1/30
 * @author Andy Ai
 */
class CouponRes extends Restlet {
  override val method: HttpMethod = HttpMethods.GET

  override val pattern: PatternMatcher = PatternMatcher("/coupons/#couponId")

  override def handle: Handle = {
    case Event(_, extractions, _, _, _) ⇒
      s"you get the coupon by id: ${extractions.asInstanceOf[Tuple1[String]]._1}"
  }
}

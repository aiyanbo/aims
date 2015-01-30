package aims.res

import aims.core.RestRes
import aims.model.Event
import aims.routing.PatternMatcher
import akka.http.model.{ HttpResponse, HttpMethods, HttpMethod }

/**
 * Component:
 * Description:
 * Date: 15/1/30
 * @author Andy Ai
 */
class CouponRes extends RestRes {
  override def method(): HttpMethod = HttpMethods.GET

  override def pattern(): PatternMatcher = PatternMatcher("/coupons/#couponId")

  override def handle: Handle = {
    case Event(_, Some(extractions), _, _) ⇒
      HttpResponse(entity = s"you get the coupon by id: ${extractions.asInstanceOf[Tuple1[String]]._1}")
  }
}

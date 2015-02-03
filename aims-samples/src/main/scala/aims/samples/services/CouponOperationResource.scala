package aims.samples.services

import aims.core.Pagination
import aims.cqrs.OperationService
import aims.model.Event
import aims.routing.PatternMatcher
import aims.routing.Patterns._
import aims.samples.dto.Coupon
import aims.util.Tuples
import akka.http.server.PathMatcher._

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource extends OperationService[Coupon] {

  override def basicPattern(): PatternMatcher = PatternMatcher(ph("coupons" ~ Slash.?))

  override def identity(event: Event): Any = Tuples.tail(event.extractions.asInstanceOf[Product]).toString

  override def identityPattern(): PatternMatcher = PatternMatcher(ph("coupons" / IntNumber ~ Slash.?))

  override def get(event: Event): Option[Coupon] = {
    Some(Coupon(1, "c-1", Some(1)))
  }

  override def pagination(event: Event): Pagination[Coupon] = {
    Pagination[Coupon](List(Coupon(1, "ff", Some(0))), 1, 1000, 1000)
  }

  override def modify(event: Event): Unit = {}

  override def update(event: Event): Unit = {}

  override def insert(event: Event): Any = {
    1
  }

  override def delete(event: Event): Unit = {}
}

package aims

import aims.core.Pagination
import aims.cqrs.OperationService
import aims.model.Event
import aims.routing.PatternMatcher
import aims.routing.Patterns._
import aims.util.Tuples
import akka.http.server.PathMatcher._

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource extends OperationService[Coupon] {
  override def basicPattern(): PatternMatcher = PatternMatcher("/coupons")

  override def identityPattern(): PatternMatcher = PatternMatcher("/coupons/#couponId")

  override def identity(event: Event): Any = Tuples.tail(event.extractions.asInstanceOf[Product])

  override def get(event: Event): Option[Coupon] = {
    throw new NullPointerException
    Some(Coupon(event.extractions.asInstanceOf[Product].productElement(0).asInstanceOf[Int], "c-1", Some(1)))
  }

  override def pagination(event: Event): Pagination[Coupon] = {
    Pagination[Coupon](List(Coupon(1, "ff", Some(0))), 1, 1000, 1000)
  }

  override def modify(event: Event): Unit = {}

  override def update(event: Event): Unit = {}

  override def insert(event: Event): Any = {
    s"insert with ${event.request.uri.path}"
  }

  override def delete(event: Event): Unit = {}
}

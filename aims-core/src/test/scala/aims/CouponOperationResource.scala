package aims

import aims.core.{ Page, Pagination }
import akka.http.model.Uri.Query

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource {
  def patterns(): (String, String) = ("/coupons", "/#couponId")

  def resourceType(): Class[Coupon] = {
    classOf[Coupon]
  }

  def get(pathParameters: Map[String, String], query: Query): Option[Coupon] = {
    None
  }

  def update(pathParameters: Map[String, String], entity: Coupon): Unit = {

  }

  def insert(pathParameters: Map[String, String], entity: Coupon): Option[Coupon] = {
    None
  }

  def delete(pathParameters: Map[String, String]): Unit = {

  }

  def pagination(pathParameters: Map[String, String], page: Page, query: Query): Pagination[Coupon] = {
    Pagination[Coupon](List(Coupon(1, "ff", Some(0))), 1, 1000, 1000)
  }
}

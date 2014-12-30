package aims

import aims.core.{OperationService, Page, Pagination}
import akka.http.model.Uri.Query

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource extends OperationService[Coupon] {
  override def patterns(): (String, String) = ("/coupons", "/#couponId")

  override def resourceType(): Class[Coupon] = {
    classOf[Coupon]
  }

  override def get(pathParameters: Map[String, String], query: Query): Option[Coupon] = {
    None
  }

  override def update(pathParameters: Map[String, String], entity: Coupon): Unit = {

  }

  override def insert(pathParameters: Map[String, String], entity: Coupon): Option[Coupon] = {
    None
  }

  override def delete(pathParameters: Map[String, String]): Unit = {

  }

  override def pagination(pathParameters: Map[String, String], page: Page, query: Query): Pagination[Coupon] = {
    Pagination[Coupon](List(Coupon(1, "ff", Some(0))), 1, 1000)
  }
}

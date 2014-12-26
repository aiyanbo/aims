package org.jmotor.samples.services

import org.jmotor.aims.core.OperationService
import org.jmotor.samples.dto.Coupon

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

  override def get(pathParameters: Map[String, String]): Option[Coupon] = {
    None
  }

  override def update(pathParameters: Map[String, String], entity: Coupon): Unit = {

  }

  override def insert(pathParameters: Map[String, String], entity: Coupon): Option[Coupon] = {
    None
  }

  override def delete(pathParameters: Map[String, String]): Unit = {

  }
}

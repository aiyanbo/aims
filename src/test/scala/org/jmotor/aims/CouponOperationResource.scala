package org.jmotor.aims

import java.util.UUID

import org.jmotor.aims.core.OperationService
import org.jmotor.aims.dto.Coupon

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource extends OperationService {
  override def patterns(): (String, String) = ("/coupons", "/#couponId")

  override def update(pathParameters: Map[String, String], entity: Any): Unit = {

  }

  override def insert(pathParameters: Map[String, String], entity: Any): Any = {
    "you-are-insert-success"
  }

  override def get(pathParameters: Map[String, String]): Any = {
    Coupon(1, UUID.randomUUID().toString)
  }

  override def delete(pathParameters: Map[String, String]): Unit = {

  }
}

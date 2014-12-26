package org.jmotor.samples.services

import java.util.UUID

import org.jmotor.aims.core.OperationService
import org.jmotor.samples.dto.Coupon

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
class CouponOperationResource extends OperationService {
  override def patterns(): (String, String) = ("/coupons", "/#couponId")

  override def resourceType(): Class[Coupon] = {
    classOf[Coupon]
  }

  override def update(pathParameters: Map[String, String], entity: Any): Unit = {
    println(entity.getClass)
  }

  override def insert(pathParameters: Map[String, String], entity: Any): Any = {
    entity match {
      case str: String ⇒ println(str)
    }
  }

  override def get(pathParameters: Map[String, String]): Coupon = {
    Coupon(1, UUID.randomUUID().toString)
  }

  override def delete(pathParameters: Map[String, String]): Unit = {

  }
}

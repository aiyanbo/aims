package org.jmotor.aims

import akka.http.model.HttpMethods
import org.jmotor.aims.core.Annotations.pattern

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
class CouponResource {

  @pattern("/coupons/#id/clim", HttpMethods.GET)
  def clim(): String = {
    ""
  }
}

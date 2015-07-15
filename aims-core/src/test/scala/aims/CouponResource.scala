package aims

import aims.core.Annotations.pattern
import akka.http.model.HttpMethods
import akka.http.scaladsl.model.HttpMethods

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

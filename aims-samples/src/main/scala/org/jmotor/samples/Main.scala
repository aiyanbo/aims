package org.jmotor.samples

import org.jmotor.aims.Aims
import org.jmotor.samples.services.{CouponOperationResource, UUIDService}

/**
 * Component:
 * Description:
 * Date: 2014/12/26
 * @author Andy Ai
 */
object Main extends App {
  val aims = Aims("hello-aims")
  aims.registerService(new UUIDService)
  aims.registerService(new CouponOperationResource)
  aims.startup()
  aims.awaitShutdown
}

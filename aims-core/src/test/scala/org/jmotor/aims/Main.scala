package org.jmotor.aims

/**
 * Component:
 * Description:
 * Date: 2014/12/26
 * @author Andy Ai
 */
class Main extends App {
  val aims = Aims("hello-aims")
  aims.registerService(new UUIDService)
  aims.registerService(new CouponOperationResource)
  aims.startup()
  aims.awaitShutdown
}

package org.jmotor.aims

import akka.actor.Props
import org.scalatest.FunSuite

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class MicroServicesTest extends FunSuite {
  test("Start micro services") {
    val aims = Aims("uuid-services")
    aims.registerService("/uuid", Props[UUIDService])
    aims.startup()
  }
}

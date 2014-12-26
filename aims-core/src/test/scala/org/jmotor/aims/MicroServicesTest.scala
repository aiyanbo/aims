package org.jmotor.aims

import org.scalatest.FunSuite

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
class MicroServicesTest extends FunSuite {
  test("Start micro services") {
    val aims = Aims("hello-aims")
    aims.registerService(new UUIDService)
    aims.registerService(new CouponOperationResource)
    aims.startup()
            aims.awaitShutdown
  }

  test("Regex") {
    val pattern = "GET::/systems/#systemId/applications/:applicationId/star"

    val tokens = pattern.split("/")
    val m = scala.collection.mutable.HashMap[String, Int]()
    for (i ← 0 to (tokens.length - 1)) {
      val token = tokens(i)
      if (token.matches("""(:|#)\w+""")) {
        m.put("""\w+""".r.findFirstIn(token).get, i)
      }
    }
    val p = pattern.replaceAll("""#\w+""", "\\\\d+").replaceAll(""":\w+""", "\\\\w+-?\\\\w+")

    val path = "GET::/systems/12345/applications/ws-456s/star"

    m.foreach(e ⇒ println(e._1, e._2))

    if (path.matches(p)) {
      val ts = path.split("/")
      m.mapValues(ts(_)).foreach(e ⇒ println(s"k: ${e._1}, v: ${e._2}"))
    }
  }
}

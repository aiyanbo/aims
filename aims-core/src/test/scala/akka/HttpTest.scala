package akka

import akka.http.model.Uri.Path
import akka.http.server.PathMatcher.{Matched, _}
import org.scalatest.FunSuite
import aims.routing.Paths._

/**
 * Component: 
 * Description:
 * Date: 15/1/13
 * @author Andy Ai
 */
class HttpTest extends FunSuite {
  test("http test") {
    val matcher = "systems" / Segment / "applications" / IntNumber / "users" / IntNumber
    val x = matcher.apply(Path("systems/1234/applications/4321/users/789"))
    m {
      path("ping" / Segment) apply Path("/ping/name")
    }

  }

  def m(m: => {}) = {
    m match {
      case Matched(_, exts) => println(exts)
      case _ => println("unmatched")
    }
  }
}

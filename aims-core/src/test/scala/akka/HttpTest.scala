package akka

import aims.routing.PatternMatcher
import aims.routing.Patterns._
import akka.http.model.Uri.Path
import akka.http.server.PathMatcher.{ Matched, _ }
import org.scalatest.FunSuite

/**
 * Component:
 * Description:
 * Date: 15/1/13
 * @author Andy Ai
 */
class HttpTest extends FunSuite {
  test("http test") {
    val matcher = "systems" / Segment / "applications" / IntNumber / "users"
    matcher / IntNumber
    m {
      matcher.apply(Path("systems/1234/applications/4321/users/789"))
    }
    m {
      ph("ping" / Segment) apply Path("/ping/name")
    }

  }

  test("Path context 1") {
    mp {
      val pattern = "systems/:systemId/applications/#applicationId"
      val matcher = PatternMatcher(pattern)
      matcher.apply(Path("systems/system-2/applications/521"))
    }
  }

  test("Path context 2") {
    mp {
      val matcher = PatternMatcher("systems" / Segment / "applications" / LongNumber)
      matcher.apply(Path("systems/system-1/applications/520"))
    }
  }

  test("Question maker") {
    val matcher = Slash ~ "system" / IntNumber
    m { matcher.apply(Path("/system")) }
    m { matcher.apply(Path("/system/123")) }
    m { matcher.apply(Path("system")) }
    m { matcher.apply(Path("system/123")) }

  }

  def m(m: ⇒ {}) = {
    m match {
      case Matched(_, exts) ⇒ println(exts.getClass)
      case _                ⇒ println("unmatched")
    }
  }

  def mp(m: ⇒ {}) = {
    m match {
      case Some(s) ⇒ println(s)
      case None    ⇒ println("unmatched")
    }
  }
}

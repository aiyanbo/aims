package akka

import akka.http.model.Uri
import akka.http.model.Uri.Path
import akka.http.server.util.Tuple
import akka.http.server.util.TupleOps.Join
import akka.http.server.{PathMatchers, PathMatcher}
import akka.http.server.PathMatcher.{Matched, _}
import org.scalatest.FunSuite
import aims.routing.Paths._

import scala.collection.mutable.ListBuffer

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
      path("ping" / Segment) apply Path("/ping/name")
    }

  }

  test("Path context 1") {
    val pattern = "systems/:systemId/applications/#applicationId"
    val matcher = PatternMatcher(pattern)
    matcher.apply(Path("systems/1234/applications/4321")) match {
      case Some(extractions) => println(extractions)
      case None => println("unmatched")
    }
  }

  test("Path context 2") {
    val pattern = "systems/:systemId/applications/#applicationId"
    val matcher = PatternMatcher("systems" / Segment / "applications" / LongNumber)
    matcher.apply(Path("systems/1234/applications/4321")) match {
      case Some(extractions) =>  println(extractions.getClass)
      case None => println("unmatched")
    }
  }

  def m(m: => {}) = {
    m match {
      case Matched(_, exts) => println(exts)
      case _ => println("unmatched")
    }
  }
}

trait PatternMatcher {
  def apply(path: Uri.Path): Option[Any]
}

class AkkaPathMatcher(pm: PathMatcher[_]) extends PatternMatcher {
  override def apply(path: Path): Option[Any] = {
    pm.apply(path) match {
      case Matched(_, extractions) => Some(extractions)
      case Unmatched => None
    }
  }
}

class AimsPathMatcher(pm: String) extends PatternMatcher {
  override def apply(path: Path): Option[Any] = {
    val tokens = pm.split("/")
    val parameters = ListBuffer[Int]()
    for (i ← 0 to (tokens.length - 1)) {
      val token = tokens(i)
      if (token.matches( """(:|#)\w+""")) {
        parameters += i
      }
    }
    val matcher = pm.replaceAll( """#\w+""", "\\\\d+").replaceAll( """:\w+""", "(\\\\w+-?)+")
    if (path.toString().matches(matcher)) {
      Some(parameters.map(path.toString().split("/")(_)))
    } else {
      None
    }
  }
}

object PatternMatcher {
  def apply(pm: PathMatcher[_]): PatternMatcher = {
    new AkkaPathMatcher(pm)
  }

  def apply(pm: String): PatternMatcher = {
    new AimsPathMatcher(pm)
  }
}
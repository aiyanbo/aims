package aims.routing

import aims.util.Lists
import akka.http.model.Uri
import akka.http.model.Uri.Path
import akka.http.server.PathMatcher
import akka.http.server.PathMatcher.{ Matched, Unmatched }

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions

/**
 * Component:
 * Description:
 * Date: 15/1/26
 * @author Andy Ai
 */

trait PatternMatcher {
  def apply(path: Uri.Path): Option[Any]

  def ~(pm: PatternMatcher): PatternMatcher

  def /(pm: PatternMatcher): PatternMatcher
}

object PatternMatcher {

  final case class AkkaPathMatcher(pm: PathMatcher[_]) extends PatternMatcher {
    override def apply(path: Path): Option[Any] = {
      pm.apply(path) match {
        case Matched(_, extractions) ⇒ Some(extractions)
        case Unmatched               ⇒ None
      }
    }

    override def ~(pm: PatternMatcher): PatternMatcher = throw new UnsupportedOperationException

    override def /(pm: PatternMatcher): PatternMatcher = throw new UnsupportedOperationException
  }

  final case class AimsPathMatcher(pm: String) extends PatternMatcher {

    override def apply(path: Path): Option[Any] = {
      val tokens = pm.split("/")
      val parameters = ListBuffer[(Int, Boolean)]()
      for (i ← 0 to (tokens.length - 1)) {
        tokens(i) match {
          case token: String if token.matches(""":\w+""") ⇒ parameters += (i -> false)
          case token: String if token.matches("""#\w+""") ⇒ parameters += (i -> true)
          case unmatched                                  ⇒
        }
      }
      val matcher = pm.replaceAll("""#\w+""", "\\\\d+").replaceAll(""":\w+""", "(\\\\w+-?)+")
      if (path.toString().matches(matcher)) {
        if (parameters.isEmpty) {
          Some(())
        } else {
          val segments = path.toString().split("/")
          Some(Lists.toTuple(parameters.map(s ⇒ if (s._2) segments(s._1).toLong else segments(s._1)).toList))
        }
      } else {
        None
      }
    }

    override def ~(pm: PatternMatcher): PatternMatcher = new AimsPathMatcher(this.pm + pm.asInstanceOf[AimsPathMatcher].pm)

    override def /(pm: PatternMatcher): PatternMatcher = new AimsPathMatcher(this.pm + "/" + pm.asInstanceOf[AimsPathMatcher].pm)
  }

  @deprecated()
  def apply(pm: PathMatcher[_]): PatternMatcher = {
    new AkkaPathMatcher(pm)
  }

  def apply(pm: String): PatternMatcher = {
    new AimsPathMatcher(pm)
  }

  implicit def stringConversion(pm: String): AimsPathMatcher = new AimsPathMatcher(pm)
}

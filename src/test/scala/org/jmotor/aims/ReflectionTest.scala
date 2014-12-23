package org.jmotor.aims

import akka.http.model.{HttpMethod, HttpMethods}
import org.jmotor.aims.core.Annotations.pattern
import org.jmotor.aims.core.Resources.ResourceMirror
import org.scalatest.FunSuite

import scala.reflect.runtime.universe._

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
class ReflectionTest extends FunSuite {

  test("Reflection") {
    val t = typeTag[CouponResource]
    t.tpe.decls.foreach(sym => {
      parseResource(sym) match {
        case None => println(s"${sym.fullName} is not resource")
        case Some(rm) => println(rm.matcher)
      }
    })
  }

  def parseResource(sym: Symbol): Option[ResourceMirror] = {
    if (sym.isPrivate) {
      return None
    }
    sym.annotations.collect {
      case annotation: Annotation if annotation.tree.tpe <:< typeOf[pattern] =>
        val args = annotation.tree.children
        val pattern = args(1) match {
          case Literal(Constant(name: String)) => Some(name)
        }

        val httpMethod = args(2) match {
          case Select(q, term) => HttpMethods.getForKey(term.decodedName.toString)
        }
        (pattern, httpMethod)
    } match {
      case Nil => None
      case metadata :: Nil =>
        val pattern: String = metadata._1.get
        val httpMethod: HttpMethod = metadata._2.get
        val tokens = pattern.split("/")
        val parameters = scala.collection.mutable.HashMap[String, Int]()
        for (i <- 0 to (tokens.length - 1)) {
          val token = tokens(i)
          if (token.matches( """(:|#)\w+""")) {
            parameters.put( """\w+""".r.findFirstIn(token).get, i)
          }
        }
        val matcher = httpMethod.name + "::" + pattern.replaceAll( """#\w+""", "\\\\d+").replaceAll( """:\w+""", "\\\\w+-?\\\\w+")
        Some(ResourceMirror(pattern, matcher, httpMethod, null, parameters.toMap))
    }
  }
}

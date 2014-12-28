package org.jmotor.aims.parser

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
object PatternParser {
  def parse(pattern: String): (String, Map[String, Int]) = {
    val tokens = pattern.split("/")
    val parameters = scala.collection.mutable.HashMap[String, Int]()
    for (i ← 0 to (tokens.length - 1)) {
      val token = tokens(i)
      if (token.matches("""(:|#)\w+""")) {
        parameters.put("""\w+""".r.findFirstIn(token).get, i)
      }
    }
    (pattern.replaceAll("""#\w+""", "\\\\d+").replaceAll(""":\w+""", "(\\\\w+-?)+"), parameters.toMap)
  }
}

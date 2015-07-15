package aims.routing

import akka.http.scaladsl.server.{ PathMatcher, PathMatchers }

/**
 * Component:
 * Description:
 * Date: 15/1/21
 * @author Andy Ai
 */
object Patterns extends PathMatchers {

  def ph[L](pm: PathMatcher[L]): PathMatcher[L] = Slash ~ pm
}

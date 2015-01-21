package aims.routing

import akka.http.server.{ PathMatchers, PathMatcher }

/**
 * Component:
 * Description:
 * Date: 15/1/21
 * @author Andy Ai
 */
object Paths extends PathMatchers {

  def path[L](pm: PathMatcher[L]): PathMatcher[L] = Slash ~ pm
}

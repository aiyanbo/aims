package aims.routing

import akka.actor.{ Props, ActorLogging, Actor }
import akka.http.model.{ HttpResponse, HttpRequest }

import akka.http.server.PathMatcher
import akka.http.server.PathMatcher.{ Unmatched, Matched }

/**
 * Component:
 * Description:
 * Date: 15/1/20
 * @author Andy Ai
 */
class RouteActor(paths: List[PathMatcher[_]]) extends Actor with ActorLogging {

  override def receive: Receive = {
    case request: HttpRequest ⇒
      paths(0).apply(request.uri.path) match {
        case Matched(_, extractions) ⇒ println(extractions)
        case Unmatched               ⇒ println("nothing")
      }
  }
}

object RouteActor {
  def props(paths: List[PathMatcher[_]]): Props = {
    Props(new RouteActor(paths))
  }
}

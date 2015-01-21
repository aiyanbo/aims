package aims

import aims.core.ServiceEngine
import aims.routing.RouteActor
import akka.actor.ActorSystem
import akka.http.Http
import akka.http.engine.server.ServerSettings
import akka.http.model._
import akka.http.server.PathMatcher
import akka.io.Inet
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Component:
 * Description:
 * Date: 15/1/6
 * @author Andy Ai
 */
class MicroServiceSystem(paths: List[PathMatcher[_]]) extends StrictLogging {

  def start(interface: String, port: Int = 80, backlog: Int = 100,
            options: immutable.Traversable[Inet.SocketOption] = Nil,
            settings: Option[ServerSettings] = None)(implicit system: ActorSystem, timeout: Timeout): Unit = {

    val router = system.actorOf(RouteActor.props(paths))

    val requestHandler: HttpRequest ⇒ Future[HttpResponse] = {
      case HttpRequest(HttpMethods.GET, Uri.Path("/favicon.ico"), _, _, _) ⇒
        Future(HttpResponse(status = StatusCodes.OK))
      case request: HttpRequest ⇒ (router ? request).map(_.asInstanceOf[HttpResponse])
    }

    implicit val flow = FlowMaterializer()
    val serverBinding = Http(system).bind(interface, port, backlog, options, settings)
    serverBinding.connections.foreach { connection ⇒
      logger.debug("Accepted new connection from " + connection.remoteAddress)
      connection handleWithAsyncHandler requestHandler
    }
  }

}

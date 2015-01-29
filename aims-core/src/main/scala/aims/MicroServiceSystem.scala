package aims

import aims.core.RestRes
import aims.http.HttpServiceBinding
import akka.actor.ActorSystem
import akka.http.Http
import akka.http.engine.server.ServerSettings
import akka.io.Inet
import akka.stream.FlowMaterializer
import akka.util.Timeout
import com.typesafe.scalalogging.StrictLogging

import scala.collection.immutable
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Component:
 * Description:
 * Date: 15/1/6
 * @author Andy Ai
 */
class MicroServiceSystem(resources: List[RestRes]) extends StrictLogging {

  def start(interface: String, port: Int = 80, backlog: Int = 100,
            options: immutable.Traversable[Inet.SocketOption] = Nil,
            settings: Option[ServerSettings] = None)(implicit system: ActorSystem, timeout: Timeout): Unit = {

    //    val requestHandler: HttpRequest ⇒ Future[HttpResponse] = {
    //      case HttpRequest(HttpMethods.GET, Uri.Path("/favicon.ico"), _, _, _) ⇒
    //        Future(HttpResponse(status = StatusCodes.OK))
    //      case request: HttpRequest ⇒ (router ? request).map(_.asInstanceOf[HttpResponse])
    //    }

    implicit val flow = FlowMaterializer()
    val serverBinding = Http(system).bind(interface, port, backlog, options, settings)

    serverBinding startHandlingWith {
      new HttpServiceBinding(system, resources).route
    }
    //    serverBinding.connections.foreach { connection ⇒
    //      logger.debug("Accepted new connection from " + connection.remoteAddress)
    //      connection handleWithAsyncHandler requestHandler
    //    }
  }

}

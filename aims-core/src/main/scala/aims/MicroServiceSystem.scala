package aims

import aims.core.Restlet
import aims.cqrs.CQRS
import aims.cqrs.CQRS.CQRS
import aims.http.HttpServiceBinding
import aims.marshalling.MarshallingActor
import akka.actor.{ ActorSystem, Props }
import akka.http.Http
import akka.http.engine.server.ServerSettings
import akka.http.model.HttpMethods
import akka.io.Inet
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout

import scala.collection.immutable

/**
 * Component:
 * Description:
 * Date: 15/1/6
 * @author Andy Ai
 */
abstract class MicroServiceSystem(resources: List[Restlet], cqrs: CQRS = CQRS.REMIX, forcedContentLengthHeader: Boolean = false)(implicit system: ActorSystem, materializer: ActorFlowMaterializer, timeout: Timeout) {

  def start(): Unit = {
    val config = system.settings.config
    start(interface = config.getString("aims.host"), port = config.getInt("aims.port"))
  }

  def start(interface: String, port: Int = 80, backlog: Int = 100,
            options: immutable.Traversable[Inet.SocketOption] = Nil,
            settings: Option[ServerSettings] = None): Unit = {
    //crate marshall actor
    system.actorOf(Props[MarshallingActor], MarshallingActor.name)

    val cqrsResources = resources.filter(r ⇒ {
      cqrs match {
        case CQRS.QUERY   ⇒ r.method == HttpMethods.GET
        case CQRS.COMMAND ⇒ r.method != HttpMethods.GET
        case CQRS.REMIX   ⇒ true
      }
    })

    val httpServiceBinding = new HttpServiceBinding(system, materializer, cqrsResources, timeout, forcedContentLengthHeader)
    val bindingRoute = httpServiceBinding.enabledMetrics(cqrs match {
      case CQRS.QUERY   ⇒ httpServiceBinding.queryRoute
      case CQRS.COMMAND ⇒ httpServiceBinding.commandRoute
      case CQRS.REMIX   ⇒ httpServiceBinding.remixRoute
    })

    import system.dispatcher
    val serverSource = Http().bind(interface, port, backlog, options, settings)
    serverSource.to(Sink.foreach { connection ⇒
      system.log.debug("Accepted new connection from " + connection.remoteAddress)
      connection handleWith bindingRoute
    }).run()

  }

}

private[aims] class MicroServiceSystemImpl(resources: List[Restlet], cqrs: CQRS = CQRS.REMIX, forcedContentLengthHeader: Boolean = false)(implicit system: ActorSystem, materializer: ActorFlowMaterializer, timeout: Timeout) extends MicroServiceSystem(resources, cqrs, forcedContentLengthHeader)(system, materializer, timeout)

object MicroServiceSystem {
  def create(resources: List[Restlet], cqrs: CQRS = CQRS.REMIX, forcedContentLengthHeader: Boolean = false)(implicit system: ActorSystem, materializer: ActorFlowMaterializer, timeout: Timeout): MicroServiceSystem = {
    new MicroServiceSystemImpl(resources, cqrs, forcedContentLengthHeader)(system, materializer, timeout)
  }
}

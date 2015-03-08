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
class MicroServiceSystem(resources: List[Restlet], cqrs: CQRS = CQRS.REMIX) {

  def start()(implicit system: ActorSystem, timeout: Timeout): Unit = {
    val config = system.settings.config
    start(interface = config.getString("aims.host"), port = config.getInt("aims.port"))
  }

  def start(interface: String, port: Int = 80, backlog: Int = 100,
            options: immutable.Traversable[Inet.SocketOption] = Nil,
            settings: Option[ServerSettings] = None)(implicit system: ActorSystem, timeout: Timeout): Unit = {
    //crate marshall actor
    system.actorOf(Props[MarshallingActor], MarshallingActor.name)

    implicit val materializer = ActorFlowMaterializer()

    val cqrsResources = resources.filter(r ⇒ {
      cqrs match {
        case CQRS.QUERY   ⇒ r.method == HttpMethods.GET
        case CQRS.COMMAND ⇒ r.method != HttpMethods.GET
        case CQRS.REMIX   ⇒ true
      }
    })

    val httpServiceBinding = new HttpServiceBinding(system, materializer, cqrsResources)
    val bindingRoute = cqrs match {
      case CQRS.QUERY   ⇒ httpServiceBinding.queryRoute
      case CQRS.COMMAND ⇒ httpServiceBinding.commandRoute
      case CQRS.REMIX   ⇒ httpServiceBinding.remixRoute
    }

    import system.dispatcher
    val serverSource = Http().bind(interface, port, backlog, options, settings)
    serverSource.to(Sink.foreach { connection ⇒
      system.log.debug("Accepted new connection from " + connection.remoteAddress)
      connection handleWith bindingRoute
    }).run()

  }

}

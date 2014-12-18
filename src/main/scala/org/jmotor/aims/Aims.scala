package org.jmotor.aims

import akka.actor.{ActorSystem, Props}
import akka.http.Http
import akka.http.model._
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import org.jmotor.aims.core.{InternalServiceApi, ServiceApi, ServiceEngine}

import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
private[aims] class Aims(name: String) {
  private val services = scala.collection.mutable.HashMap[String, InternalServiceApi]()
  implicit val system = ActorSystem(name)
  implicit val materializer = FlowMaterializer()
  implicit val timeout: Timeout = 5000.millis

  def startup(): Unit = {
    val serverEngine = system.actorOf(ServiceEngine.props(services.toMap), "aims-engine")

    val requestHandler: HttpRequest => Future[HttpResponse] = {
      case HttpRequest(HttpMethods.GET, Uri.Path("/favicon.ico"), _, _, _) =>
        Future(HttpResponse(status = StatusCodes.OK))
      case request: HttpRequest => (serverEngine ? request).map(_.asInstanceOf[HttpResponse])
      case _ => Future(HttpResponse(StatusCodes.NotFound, entity = "Unknown resource!"))
    }

    val serverBinding = Http(system).bind(interface = "localhost", port = 8080)
    for (connection <- serverBinding.connections) {
      println("Accepted new connection from " + connection.remoteAddress)
      connection handleWithAsyncHandler requestHandler
    }
  }

  def registerService(service: ServiceApi): Unit = {
    val pattern = service.method.name + "::" + service.pattern
    val tokens = pattern.split("/")
    val m = scala.collection.mutable.HashMap[String, Int]()
    for (i <- 0 to (tokens.length - 1)) {
      val token = tokens(i)
      if (token.matches( """:\w+(=>Number)?""")) {
        m.put( """\w+""".r.findFirstIn(token).get, i)
      }
    }
    val p = pattern.replaceAll( """:\w+=>Number""", "\\\\d+").replaceAll( """:\w+""", "\\\\w+-?\\\\w+")
    services.put(p, InternalServiceApi(service, m.toMap))
  }

  def registerService(pattern: String, props: Props): Unit = {
    registerService(new ServiceApi(pattern, HttpMethods.GET, props))
  }

  lazy val shutdown = system.shutdown()

  lazy val awaitShutdown = system.awaitTermination()
}

object Aims {
  def apply(): Aims = {
    apply("aims")
  }

  def apply(name: String): Aims = {
    new Aims(name)
  }
}
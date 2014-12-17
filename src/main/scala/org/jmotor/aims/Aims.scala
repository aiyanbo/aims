package org.jmotor.aims

import akka.actor.{ActorSystem, Props}
import akka.http.Http
import akka.http.model._
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import org.jmotor.aims.core.{ServiceApi, ServiceEngine}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._


/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
private[aims] class Aims(name: String) {
  private val apis = scala.collection.mutable.HashMap[String, ServiceApi]()
  implicit val system = ActorSystem(name)
  implicit val materializer = FlowMaterializer()
  implicit val timeout: Timeout = 5000.millis

  def startup(): Unit = {
    val serverEngine = system.actorOf(ServiceEngine.props(apis.toMap), "aims-engine")

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
    apis.put(service.pattern, service)
  }

  def registerService(pattern: String, props: Props): Unit = {
    apis.put(pattern, new ServiceApi(pattern, HttpMethods.GET, props))
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
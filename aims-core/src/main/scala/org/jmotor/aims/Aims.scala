package org.jmotor.aims

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model._
import akka.pattern.ask
import akka.stream.FlowMaterializer
import akka.util.Timeout
import org.jmotor.aims.core.Resources.Resource
import org.jmotor.aims.core.{ OperationService, Service, ServiceEngine }

import scala.collection.mutable.ListBuffer
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
  implicit val system = ActorSystem(name)
  implicit val materializer = FlowMaterializer()
  implicit val timeout: Timeout = 5000.millis

  private val resources = ListBuffer[Resource]()

  def startup(): Unit = {
    val serverEngine = system.actorOf(ServiceEngine.props(resources.toList), "aims-engine")

    val requestHandler: HttpRequest ⇒ Future[HttpResponse] = {
      case HttpRequest(HttpMethods.GET, Uri.Path("/favicon.ico"), _, _, _) ⇒
        Future(HttpResponse(status = StatusCodes.OK))
      case request: HttpRequest ⇒ (serverEngine ? request).map(_.asInstanceOf[HttpResponse])
      case _                    ⇒ Future(HttpResponse(StatusCodes.NotFound, entity = "Unknown resource!"))
    }

    val serverBinding = Http(system).bind(interface = "localhost", port = 8080)
    for (connection ← serverBinding.connections) {
      println("Accepted new connection from " + connection.remoteAddress)
      connection handleWithAsyncHandler requestHandler
    }
  }

  def registerService(service: Service): Unit = {
    resources += service.resource()
  }

  def registerService(service: OperationService): Unit = {
    resources ++= service.resources()
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

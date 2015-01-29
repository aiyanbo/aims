package akka

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.{ HttpRequest, HttpResponse, StatusCodes }
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Flow

import scala.concurrent.Future

object TestStream extends App {
  implicit val system = ActorSystem("my-test")

  import akka.TestStream.system.dispatcher

  implicit val materializer = FlowMaterializer()

  //  import akka.http.marshallers.xml.ScalaXmlSupport._

  val binding = Http().bind(interface = "localhost", port = 8080)

  val flow = Flow[HttpRequest].via(Flow[HttpRequest].mapAsync(req ⇒ Future {
    StatusCodes.NotFound
  })).mapAsync {
    status ⇒
      Future {
        HttpResponse(entity = status.intValue.toString)
      }
  }

  val materializedMap = binding startHandlingWith flow
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  scala.io.StdIn.readLine()
  binding.unbind(materializedMap).onComplete(_ ⇒ system.shutdown())
}

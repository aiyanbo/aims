package akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteResult
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.google.common.net.HttpHeaders

object TestServer extends App {
  implicit val system = ActorSystem("my-test")

  import akka.TestServer.system.dispatcher

  implicit val materializer = ActorMaterializer()

  //  import akka.http.marshallers.xml.ScalaXmlSupport._

  val route = {
    get {
      extractRequestContext {
        ctx ⇒
          complete(ctx.request.uri.path.toString())
      }
    } ~ put {
      optionalHeaderValueByName(HttpHeaders.CONTENT_TYPE) {
        case Some(header) ⇒
          extractRequestContext {
            ctx ⇒ complete(ctx.request.uri.path.toString())
          }
        case None ⇒ complete("")
      }
    } ~ post {
      RouteResult.Complete(HttpResponse(entity = "you have post")) match {
        case RouteResult.Complete(response) ⇒ complete(response)
      }
    }
  }

  val serverSource = Http().bind(interface = "localhost", port = 8080)
  val bindingFuture = serverSource.to(Sink.foreach { connection ⇒
    println("Accepted new connection from " + connection.remoteAddress)
    connection handleWith route
  }).run()
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  scala.io.StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ ⇒ system.shutdown())
}

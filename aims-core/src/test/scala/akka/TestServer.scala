package akka

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.Directives._
import akka.http.server.RouteResult
import akka.stream.ActorFlowMaterializer
import com.google.common.net.HttpHeaders

object TestServer extends App {
  implicit val system = ActorSystem("my-test")

  import akka.TestServer.system.dispatcher

  implicit val materializer = ActorFlowMaterializer()

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

  val bindingFuture = Http().bindAndstartHandlingWith(route, interface = "localhost", port = 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  scala.io.StdIn.readLine()
  bindingFuture.flatMap(_.unbind()).onComplete(_ ⇒ system.shutdown())
}

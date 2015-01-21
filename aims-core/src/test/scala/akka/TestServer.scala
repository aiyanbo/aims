package akka

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.HttpResponse
import akka.stream.FlowMaterializer
import akka.http.server.Directives._

object TestServer extends App {
  implicit val system = ActorSystem("my-test")

  import akka.TestServer.system.dispatcher

  implicit val materializer = FlowMaterializer()

  //  import akka.http.marshallers.xml.ScalaXmlSupport._


  val binding = Http().bind(interface = "localhost", port = 8080)

  val x = {
    get {
      path("") {
        complete(HttpResponse(entity = "you are index"))
      } ~
        path("secure") {
          complete("Hello . Access has been granted!")

        } ~
        path("ping") {
          complete("PONG!")
        } ~
        path("crash") {
          complete(sys.error("BOOM!"))
        }
    }
  }



  val materializedMap = binding startHandlingWith x

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()
  binding.unbind(materializedMap).onComplete(_ ⇒ system.shutdown())

  lazy val index =
    <html>
      <body>
        <h1>Say hello to
          <i>akka-http-core</i>
          !</h1>
        <p>Defined resources:</p>
        <ul>
          <li>
            <a href="/ping">/ping</a>
          </li>
          <li>
            <a href="/secure">/secure</a>
            Use any username and '
            &lt;
            username
            &gt;
            -password' as credentials</li>
          <li>
            <a href="/crash">/crash</a>
          </li>
        </ul>
      </body>
    </html>
}

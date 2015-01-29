package aims

import aims.res.PingRes
import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.util.Timeout

import scala.concurrent.duration._

/**
 * Component:
 * Description:
 * Date: 15/1/20
 * @author Andy Ai
 */
object SimpleApp extends App {
  implicit val system = ActorSystem("aims")
  implicit val materializer = FlowMaterializer()
  implicit val timeout: Timeout = 5000.millis

  private val service = new MicroServiceSystem(List(new PingRes()))

  service.start("localhost", port = 8080)
}

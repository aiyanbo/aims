package aims.samples

import aims.MicroServiceSystem
import aims.samples.services.CouponOperationResource
import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.util.Timeout

import scala.concurrent.duration._

/**
 * Component:
 * Description:
 * Date: 2014/12/26
 * @author Andy Ai
 */
object Main extends App {
  implicit val system = ActorSystem("aims")
  implicit val materializer = FlowMaterializer()
  implicit val timeout: Timeout = 5000.millis

  private val service = new MicroServiceSystem(new CouponOperationResource().resources())

  service.start("localhost", port = 8080)
}

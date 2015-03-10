package aims

import aims.res.{ FileUploadRes, FileDownloadRes, CheckNameRes, PingRes }
import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
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
  implicit val timeout: Timeout = 5000.millis
  implicit val materializer = ActorFlowMaterializer()

  private val service = MicroServiceSystem.create(List(new PingRes, new CheckNameRes, new FileDownloadRes, new FileUploadRes) ++ new CouponOperationResource().resources())

  service.start()
}

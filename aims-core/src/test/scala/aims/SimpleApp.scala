package aims

import aims.res.{CheckNameRes, FileDownloadRes, FileUploadRes, PingRes}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
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
  implicit val materializer = ActorMaterializer()

  private val service = MicroServiceSystem.create(List(new PingRes, new CheckNameRes, new FileDownloadRes, new FileUploadRes) ++ new CouponOperationResource().resources())

  service.start()
}

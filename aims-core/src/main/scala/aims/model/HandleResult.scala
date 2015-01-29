package aims.model

import akka.http.model.HttpResponse
import akka.http.server.Rejection

import scala.collection.immutable

/**
 * Component:
 * Description:
 * Date: 15/1/28
 * @author Andy Ai
 */
sealed trait HandleResult

object HandleResult {

  final case class Success(result: Any) extends HandleResult

  final case class Complete(response: HttpResponse) extends HandleResult

  final case class Rejected(rejections: immutable.Seq[Rejection]) extends HandleResult

}

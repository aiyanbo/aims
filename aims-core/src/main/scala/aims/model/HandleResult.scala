package aims.model

import akka.http.model.HttpResponse
import akka.http.server.Rejection

import scala.collection.immutable
import scala.language.implicitConversions

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

  final case class Failure(causes: immutable.Seq[Throwable]) extends HandleResult

  final case class Rejected(rejections: immutable.Seq[Rejection]) extends HandleResult

  implicit def success(result: Any): Success = {
    Success(result)
  }

  implicit def complete(response: HttpResponse): Complete = {
    Complete(response)
  }

  implicit def failure(causes: immutable.Seq[Throwable]): Failure = {
    Failure(causes)
  }

  implicit def rejected(rejections: immutable.Seq[Rejection]): Rejected = {
    Rejected(rejections)
  }

}

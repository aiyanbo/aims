package aims.core

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
sealed trait RestletResult

object RestletResult {

  final case class Success(result: Any) extends RestletResult

  final case class Complete(response: HttpResponse) extends RestletResult

  final case class Failure(causes: immutable.Seq[Throwable]) extends RestletResult

  final case class Rejected(rejections: immutable.Seq[Rejection]) extends RestletResult

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

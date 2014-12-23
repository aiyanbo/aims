package org.jmotor.aims.core

import akka.http.model.{ HttpMethod, HttpMethods, MediaType, MediaTypes }

import scala.annotation.StaticAnnotation

/**
 * Component:
 * Description:
 * Date: 2014/12/23
 * @author Andy Ai
 */
object Annotations {

  final case class pattern(pattern: String, method: HttpMethod = HttpMethods.GET) extends StaticAnnotation

  final case class mediaTypes(
    consumers: List[MediaType] = List(MediaTypes.`application/json`),
    producers: List[MediaType] = List(MediaTypes.`application/json`)
  ) extends StaticAnnotation

}

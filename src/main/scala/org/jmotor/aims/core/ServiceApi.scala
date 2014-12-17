package org.jmotor.aims.core

import akka.actor.Props
import akka.http.model.{HttpMethod, MediaType, MediaTypes}

/**
 * Component:
 * Description:
 * Date: 2014/12/17
 * @author Andy Ai
 */
case class ServiceApi(pattern: String, method: HttpMethod, props: Props,
                      consumerTypes: List[MediaType] = List(MediaTypes.`application/json`),
                      producerTypes: List[MediaType] = List(MediaTypes.`application/json`))

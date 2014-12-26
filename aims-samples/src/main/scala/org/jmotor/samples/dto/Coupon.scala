package org.jmotor.samples.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
case class Coupon(id: Long, name: String, @JsonDeserialize(contentAs = classOf[java.lang.Long]) status: Option[Long])
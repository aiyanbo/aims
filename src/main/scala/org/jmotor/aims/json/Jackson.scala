package org.jmotor.aims.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper, PropertyNamingStrategy, SerializationFeature }
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
 * Component:
 * Description:
 * Date: 2014/12/24
 * @author Andy Ai
 */
object Jackson {
  lazy val mapper = new ObjectMapper()

  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
  mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
  mapper.registerModule(DefaultScalaModule)
}

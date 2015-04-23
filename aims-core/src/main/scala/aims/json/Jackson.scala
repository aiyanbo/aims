package aims.json

import java.text.SimpleDateFormat

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind._
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
  mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
  mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"))
  mapper.registerModule(DefaultScalaModule)
}

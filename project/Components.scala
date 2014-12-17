import sbt._


object Version {
  val akka = "2.3.7"
  val scalaTest = "2.2.2"
}

object Components {
  val dependencies = List(
    "org.scalatest" %% "scalatest" % Version.scalaTest % "test"
  )
}
import sbt._

object Version {
  val akka = "2.3.7"
  val akkaHttp = "1.0-M1"
  val scalaTest = "2.2.2"
}

object Dependencies {
  val libraries = List(
    "com.typesafe.akka" %% "akka-actor" % Version.akka withSources(),
    "com.typesafe.akka" %% "akka-stream-experimental" % Version.akkaHttp withSources(),
    "com.typesafe.akka" %% "akka-http-core-experimental" % Version.akkaHttp withSources(),
    "org.scalatest" %% "scalatest" % Version.scalaTest % "test" withSources()
  )
}
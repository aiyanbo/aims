package aims

import sbt._

object Dependencies {

  object Versions {
    val akka = "2.3.7"
    val akkaHttp = "1.0-M1"
    val scalaTest = "2.2.2"
  }

  val libraries = List(
    "com.typesafe.akka" %% "akka-actor" % Versions.akka withSources(),
    "com.typesafe.akka" %% "akka-stream-experimental" % Versions.akkaHttp withSources(),
    "com.typesafe.akka" %% "akka-http-core-experimental" % Versions.akkaHttp withSources(),
    "org.scalatest" %% "scalatest" % Versions.scalaTest % "test" withSources()
  )

}
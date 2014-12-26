package aims

import sbt._

object Dependencies {

  object Versions {
    val akka = "2.3.7"
    val jackson = "2.4.4"
    val akkaHttp = "1.0-M2"
    val scalaTest = "2.2.2"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  }

  object Compile {
    val akka = "com.typesafe.akka" %% "akka-actor" % Versions.akka
    val akkaStream = "com.typesafe.akka" %% "akka-stream-experimental" % Versions.akkaHttp
    val akkaHttp = "com.typesafe.akka" %% "akka-http-core-experimental" % Versions.akkaHttp
    val jackson = "com.fasterxml.jackson.module" %% "jackson-module-scala" % Versions.jackson
  }

  import Compile._

  val core = Seq(akka, akkaStream, akkaHttp, jackson, Test.scalaTest)
}

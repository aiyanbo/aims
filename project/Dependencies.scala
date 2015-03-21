package aims

import sbt._

object Dependencies {

  object Versions {
    val akka = "2.3.9"
    val log4j2 = "2.1"
    val guava = "18.0"
    val slick = "2.1.0"
    val scala = "2.11.6"
    val jackson = "2.5.1"
    val akkaHttp = "1.0-M4"
    val scalaTest = "2.2.2"
    val scalaLogging = "3.1.0"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  }

  object Compile {
    val guava = "com.google.guava" % "guava" % Versions.guava
    val slick = "com.typesafe.slick" %% "slick" % Versions.slick
    val akka = "com.typesafe.akka" %% "akka-actor" % Versions.akka
    val log4j2Api = "org.apache.logging.log4j" % "log4j-api" % Versions.log4j2
    val log4j2Core = "org.apache.logging.log4j" % "log4j-core" % Versions.log4j2
    val log4j2Slf4j = "org.apache.logging.log4j" % "log4j-slf4j-impl" % Versions.log4j2
    val akkaStream = "com.typesafe.akka" %% "akka-stream-experimental" % Versions.akkaHttp
    val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % Versions.akkaHttp
    val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core-experimental" % Versions.akkaHttp
    val jackson = "com.fasterxml.jackson.module" %% "jackson-module-scala" % Versions.jackson
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
  }

  import aims.Dependencies.Compile._

  val core = Seq(log4j2Api, log4j2Core, log4j2Slf4j, guava, scalaLogging, akka, akkaStream, akkaHttp, akkaHttpCore, jackson, Test.scalaTest)

  val slick = Seq(Compile.slick, Test.scalaTest)

}

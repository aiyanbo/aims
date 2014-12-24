package aims

import sbt._

object Dependencies {

  val libraries = Seq(Compile.akka, Compile.akkaStream, Compile.akkaHttp, Test.scalaTest)

  object Versions {
    val akka = "2.3.7"
    val akkaHttp = "1.0-M2"
    val scalaTest = "2.2.2"
  }

  object Test {
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test" withSources()
  }

  object Compile {
    val akka = "com.typesafe.akka" %% "akka-actor" % Versions.akka withSources()
    val akkaStream = "com.typesafe.akka" %% "akka-stream-experimental" % Versions.akkaHttp withSources()
    val akkaHttp = "com.typesafe.akka" %% "akka-http-core-experimental" % Versions.akkaHttp withSources()
  }

}

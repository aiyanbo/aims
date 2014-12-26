package aims

import sbt.Keys._
import sbt._

object AimsBuild extends Build {
  lazy val buildSettings = Seq(
    organization := "org.jmotor.aims",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.4"
  )

  lazy val root = (project in file(".")).aggregate(core)

  lazy val core = Project(
    id = "aims-core",
    base = file("aims-core")
  )
}
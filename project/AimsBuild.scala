package aims

import aims.Dependencies.Versions
import sbt.Keys._
import sbt._

object AimsBuild extends Build {
  lazy val buildSettings = net.virtualvoid.sbt.graph.Plugin.graphSettings ++: Seq(
    organization := "org.jmotor.aims",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := Versions.scala
  )

  lazy val root = (project in file(".") settings(publish := {}, publishLocal := {})).aggregate(core, slick)

  lazy val core = Project(
    id = "aims-core",
    base = file("aims-core")
  )

  lazy val slick = Project(
    id = "aims-slick",
    base = file("aims-slick"),
    dependencies = Seq(core)
  )
}
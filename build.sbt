organization := "davidillsley"

name := "casbah-jodaGridFs-prob"

version := "0.1.5-SNAPSHOT"

scalacOptions += "-deprecation"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
        "org.mongodb" %% "casbah" % "2.6.2"
    )
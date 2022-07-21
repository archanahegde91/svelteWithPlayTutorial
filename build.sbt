name := """PollApp"""
organization := "com.informaticon"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  ws,
  javaJdbc,
  evolutions,
  "com.h2database" % "h2" % "1.4.199",
  "mysql" % "mysql-connector-java" % "8.0.22"
)

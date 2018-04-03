name := """DataMapper"""
organization := "com.example"

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayScala)


scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
libraryDependencies ++= Seq("com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "org.json" % "json" % "20140107",
)
// https://mvnrepository.com/artifact/org.apache.poi/poi
libraryDependencies += "org.apache.poi" % "poi" % "3.15"// https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.15"


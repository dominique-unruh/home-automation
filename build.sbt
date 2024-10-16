ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.1"

lazy val root = (project in file("."))
  .settings(
        name := "home-automation",
        idePackagePrefix := Some("de.unruh.homeautomation"),
        resolvers += Resolver.jcenterRepo,
        libraryDependencies += "org.eclipse.paho" % "org.eclipse.paho.mqttv5.client" % "1.2.5",
        libraryDependencies += "com.lihaoyi" %% "upickle" % "4.0.2",
        libraryDependencies += "io.monix" %% "monix-reactive" % "3.4.1",
  )

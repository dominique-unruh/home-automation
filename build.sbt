import scala.sys.process.Process

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(JavaAppPackaging)

lazy val root = (project in file("."))
  .settings(
    name := "home-automation",
    idePackagePrefix := Some("de.unruh.homeautomation"),
    resolvers += Resolver.jcenterRepo,
    //        libraryDependencies += "org.eclipse.paho" % "org.eclipse.paho.mqttv5.client" % "1.2.5",
    libraryDependencies ++= Seq(
      "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.2.5",
      "com.lihaoyi" %% "upickle" % "4.0.2",
      "io.monix" %% "monix-reactive" % "3.4.1",
      "commons-io" % "commons-io" % "2.18.0",
      "org.scalatra" %% "scalatra-jakarta" % "3.1.1",
      "org.eclipse.jetty" % "jetty-server" % "12.0.16",
      "org.slf4j" % "slf4j-simple" % "2.0.16",
    )
  )

lazy val installOnDjinn = taskKey[Unit]("Install the package on djinn")

installOnDjinn := {
  println("Uploading to djinn")
  Process(List("rsync", "-e", "ssh -p 26522", "-a", "--", stage.value.toString+"/", "unruh@djinn:/opt/home-automation")).!
  println("Restarting service on djinn")
  Process("ssh -p 26522 djinn sudo systemctl restart home-automation").!
  println("Successfully updated")
}

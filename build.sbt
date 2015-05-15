name := """storytellers"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0-3",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "dustjs-linkedin" % "2.6.1",
  "org.webjars" % "font-awesome" % "4.3.0",
  "org.webjars" % "jquery-file-upload" % "9.8.1",
  "org.webjars.bower" % "blueimp-file-upload" % "9.9.3",
  "org.webjars.bower" % "blueimp-load-image" % "1.13.0",
  "org.webjars.bower" % "blueimp-canvas-to-blob" % "2.1.1",
  "com.typesafe.play" %% "play-slick" % "0.8.1",
  "com.typesafe.play" %% "play-mailer" % "2.4.0",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT",
  "joda-time" % "joda-time" % "2.4",
  "org.joda" % "joda-convert" % "1.6",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.imgscalr" % "imgscalr-lib" % "4.2"
)

resolvers += 
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

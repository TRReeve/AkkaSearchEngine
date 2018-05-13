name := "AkkaWebCrawler"

version := "0.1"

scalaVersion := "2.12.6"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "org.jsoup" % "jsoup" % "1.8.3",
  "commons-validator" % "commons-validator" % "1.5+",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12"
)
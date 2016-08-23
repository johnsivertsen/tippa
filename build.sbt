name := """tippa"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  ws,
  "com.typesafe.slick"  %% "slick"                  % "3.1.1",
  "com.typesafe.play"   %% "play-slick"             % "2.0.0",
  "com.typesafe.play"   %% "play-slick-evolutions"  % "2.0.0",
  "com.h2database"      % "h2"                      % "1.4.190",
  "org.mindrot"         % "jbcrypt"                 % "0.3m"
)

CoffeeScriptKeys.bare := true
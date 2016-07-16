import play.sbt.PlayScala
import sbt._
import sbt.Keys._

object Build extends Build {

	val appScalaVersion = "2.11.7"

	lazy val main = Project("demo", base = file(".")).enablePlugins(PlayScala).settings(
		scalaVersion := appScalaVersion,
		libraryDependencies ++= List(
			"com.typesafe.slick"  %% "slick"                  % "3.1.0",
			"com.typesafe.play"   %% "play-slick"             % "1.1.1",
			"com.typesafe.play"   %% "play-slick-evolutions"  % "1.1.1",
			"com.h2database"      % "h2"                      % "1.4.190"
		)
	)
}
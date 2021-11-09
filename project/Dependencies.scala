import sbt._

object Dependencies {

  val scala_test = "org.scalatest" %% "scalatest" % "3.2.9" % Test
  val logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
  val cats = "org.typelevel" %% "cats-core" % "2.6.1"

  val slick = "com.typesafe.slick" %% "slick" % "3.3.3"
  val slickCodeGen = "com.typesafe.slick" %% "slick-codegen" % "3.3.3"
  val postgresql = "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"
  val h2 = "com.h2database" % "h2" % "1.4.199" % Test
  val hikariCP = "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3"

  val slick_deps = Seq(slick, slickCodeGen, postgresql, h2, hikariCP)

  val circeVersion = "0.14.1"

  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion) :+ "de.heikoseeberger" %% "akka-http-circe" % "1.38.2"

  val cacheVersion = "0.28.0"
  val cache = Seq(
    "com.github.cb372" %% "scalacache-core",
    "com.github.cb372" %% "scalacache-caffeine",
    "com.github.cb372" %% "scalacache-cats-effect"
  ).map(_ % cacheVersion)

  val other_essentials = Seq(logging, scala_test, cats)

  val dwar_deps = slick_deps ++ circe ++ cache ++ other_essentials

}

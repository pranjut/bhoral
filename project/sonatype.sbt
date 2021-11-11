/*
import xerial.sbt.Sonatype._

sonatypeProfileName := "com.pranjutgogoi"

ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("pranjut", "bhoral", "admin@pranjutgogoi.com"))

// or if you want to set these fields manually
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/pranjut/bhoral"),
    "scm:git@github.com:pranjut/bhoral.git"
  )
)
ThisBuild / developers := List(
  Developer(id="pranjut", name="Pranjut Gogoi", email="admin@pranjutgogoi.com", url=url("https://pranjutgogoi.com"))
)

credentials += Credentials(Path.userHome / ".sbt" / ".sonatype_credentials")

ThisBuild / description := "A simple library which wraps slick and scala cache together"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://pranjutgogoi.com"))

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
*/

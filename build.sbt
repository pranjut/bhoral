import Dependencies._
import ReleaseTransformations._

name := "bhoral"
organization := "com.pranjutgogoi"

transitiveClassifiers in Global := Seq(Artifact.SourceClassifier, Artifact.DocClassifier)

publishMavenStyle := true
Test / publishArtifact := false
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

val topDir = file(".")
scalaVersion := "2.13.7"


lazy val assemblySettings = Seq(
  assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false),
  assembly / assemblyOutputPath := baseDirectory.value / "target" / "output" / s"${organization.value}-${name.value}.jar",
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF","services",xs @ _*) => MergeStrategy.filterDistinctLines
    case PathList("META-INF",xs @ _*) => MergeStrategy.discard
    case "application.conf" => MergeStrategy.concat
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
)


lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(organization := "com.pranjutgogoi", scalaVersion := "2.13.5")
  ),
  name := "bhoral",
  libraryDependencies ++= dwar_deps
).enablePlugins(AshScriptPlugin)
  .settings(assemblySettings)
  .settings(
    assemblyPackageScala / assembleArtifact := false,
    assemblyPackageDependency / assembleArtifact := false,
    packageDoc / publishArtifact := true,
    packageSrc / publishArtifact := true,
    exportJars := true,
    crossPaths := true
  )
  .settings(addArtifact( Compile / artifact, assembly).settings: _*)


import xerial.sbt.Sonatype._

ThisBuild / sonatypeProfileName := "com.pranjutgogoi"

ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("pranjut", "bhoral", "admin@pranjutgogoi.com"))

// or if you want to set these fields manually
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/pranjut/bhoral"),
    "scm:git@github.com:pranjut/bhoral.git"
  )
)
ThisBuild / developers := List(
  Developer(id="pranjut", name="pranjut", email="pranjut.gogoi@gmail.com", url=url("https://pranjutgogoi.com"))
)

credentials += Credentials(Path.userHome / ".sbt" / ".sonatype_credentials")

/*ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}*/

publishTo := sonatypePublishToBundle.value



ThisBuild / description := "A simple library which wraps slick and scala cache together"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://pranjutgogoi.com"))

ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishMavenStyle := true

//usePgpKeyHex("DD62A837ECA8637B")
//pgpSigningKey:= Some("DD62A837ECA8637B")
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost := "s01.oss.sonatype.org"

Global / pgpPassphrase := sys.env.get("PGP_PASS").map(_.toArray)


Test / parallelExecution := false

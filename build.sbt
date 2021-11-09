import Dependencies._


name := "bhoral"
organization := "com.pranjutgogoi"



transitiveClassifiers in Global := Seq(Artifact.SourceClassifier, Artifact.DocClassifier)

publishMavenStyle := true
Test / publishArtifact := false
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

val topDir = file(".")
scalaVersion := "2.12.14"


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
    List(organization := "com.pranjutgogoi.bhoral", scalaVersion := "2.13.5")
  ),
  name := "bhoral",
  libraryDependencies ++= dwar_deps
).enablePlugins(AshScriptPlugin)
  .settings(assemblySettings)
  .settings(
    assemblyPackageScala / assembleArtifact := false,
    assemblyPackageDependency / assembleArtifact := false,
    packageDoc / publishArtifact := false,
    packageSrc / publishArtifact := false,
    exportJars := true,
    crossPaths := true
  )
  .settings(addArtifact( Compile / artifact, assembly).settings: _*)



Test / parallelExecution := false

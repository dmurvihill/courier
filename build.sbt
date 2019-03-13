addCommandAlias("ci", ";clean ;+test")
addCommandAlias("release", ";project root ;+publishSigned ;sonatypeReleaseAll")

lazy val gpgSettings = Seq(
  useGpg := true
)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val publisherSettings = Seq(
  sonatypeProfileName := organization.value,
  publishMavenStyle := true,
  publishTo := Some(
    if (isSnapshot.value)
      Opts.resolver.sonatypeSnapshots
    else
      Opts.resolver.sonatypeStaging
  )
)

lazy val releaseSettings = gpgSettings ++ publisherSettings ++ credentialSettings

lazy val commonSettings = releaseSettings ++ Seq(
  version := "1.0.0",
  organization := "com.github.daddykotex",
  description := "deliver electronic mail with scala",
  licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT"))),
  homepage := Some(url("https://github.com/dmurvihill/courier")),
  crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.8"),
  scalaVersion := crossScalaVersions.value.last,
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/dmurvihill/courier"),
      "scm:git@github.com:dmurvihill/courier.git"
    )
  ),
  developers := List(
    Developer(
      id = "daddykotex",
      name = "David Francoeur",
      email = "noreply@davidfrancoeur.com",
      url = url("https://davidfrancoeur.com")
    ),
    Developer(
      id = "dmurvihill",
      name = "Dolan Murvihill",
      email = "dmurvihill@gmail.com",
      url = url("https://github.com/dmurvihill")
    )
  )
)

lazy val cFlags = Seq(
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n <= 10 =>
      Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions"
      )
    case Some((2, n)) if n == 11 =>
      Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Ypartial-unification"
      )
    case _ =>
      ScalacOptions.All
  })
)

lazy val root = (project in file("."))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier",
    libraryDependencies ++= Seq(
      "com.sun.mail"      % "javax.mail"      % "1.6.2",
      "javax.activation"  % "activation"      % "1.1.1",
      "org.bouncycastle"  % "bcpkix-jdk15on"  % "1.61" % Optional,
      "org.bouncycastle"  % "bcmail-jdk15on"  % "1.61" % Optional,
      "org.scalactic"     %% "scalactic"      % "3.0.5" % Test,
      "org.scalatest"     %% "scalatest"      % "3.0.5" % Test,
      "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % Test
    )
  )

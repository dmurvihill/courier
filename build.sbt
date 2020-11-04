addCommandAlias("ci", ";clean ;+test")
addCommandAlias("release", ";project root ;+publish ;sonatypeReleaseAll")

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

lazy val releaseSettings = publisherSettings ++ credentialSettings

lazy val commonSettings = releaseSettings ++ Seq(
  version := "3.0.0-RC1",
  organization := "com.github.daddykotex",
  description := "deliver electronic mail with scala",
  licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT"))),
  homepage := Some(url("https://github.com/dmurvihill/courier")),
  crossScalaVersions := Seq("2.11.12", "2.12.12", "2.13.3", "0.27.0-RC1"),
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
    case Some((2, 11)) =>
      Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Ypartial-unification"
      )
    case Some((2, 12)) =>
      ScalacOptions.All ++ Seq(
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Xfuture",
        "-Xlint:by-name-right-associative",
        "-Xlint:unsound-match",
        "-Yno-adapted-args",
        "-Ypartial-unification",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit"
      )
    case Some((2, 13)) =>
      ScalacOptions.All
    case Some((0, 27)) =>
      Seq.empty
    case Some(_) | None =>
      throw new IllegalArgumentException("Unable to figure out scalacOptions")
  }),
  scalacOptions in Test := {
    if (isDotty.value) { Seq("-language:implicitConversions") } else { Seq.empty }
  }
)

lazy val scalaTestVersion = "3.2.2"

lazy val root = (project in file("."))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier",
    libraryDependencies ++= Seq(
      "com.sun.mail"      % "javax.mail"      % "1.6.2",
      "javax.activation"  % "activation"      % "1.1.1",
      "org.bouncycastle"  % "bcpkix-jdk15on"  % "1.61" % Optional,
      "org.bouncycastle"  % "bcmail-jdk15on"  % "1.61" % Optional,
      "org.scalactic"     %% "scalactic"      % scalaTestVersion % Test,
      "org.scalatest"     %% "scalatest"      % scalaTestVersion % Test,
      "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % Test
    )
  )

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

def java8Options(scalaVersion: String): Seq[String] =
    if (scalaVersion.startsWith("2.12") || scalaVersion.startsWith("2.11")) Seq("-target:jvm-1.8", "-release", "8")
    else if (scalaVersion.startsWith("2.13")) Seq("-target:8", "-release", "8")
    else if (scalaVersion.startsWith("3.")) Seq("-release", "8")
    else sys.error(s"Unsupported scala version: $scalaVersion")

lazy val commonSettings = releaseSettings ++ Seq(
  version := "4.0.0-RC1",
  organization := "com.github.daddykotex",
  description := "deliver electronic mail with scala",
  licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT"))),
  homepage := Some(url("https://github.com/dmurvihill/courier")),
  crossScalaVersions := Seq("2.11.12", "2.12.12", "2.13.4", "3.0.1", "3.1.0"),
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
    case Some((3, 0)) =>
      Seq("-rewrite")
    case Some((3, 1)) =>
      Seq("-rewrite")
    case Some(_) | None =>
      throw new IllegalArgumentException("Unable to figure out scalacOptions")
  })
)

lazy val courier = (project in file("courier"))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier",
    libraryDependencies ++= Seq(
      "com.sun.mail"      % "javax.mail"      % "1.6.2",
      "javax.activation"  % "activation"      % "1.1.1",
      "org.bouncycastle"  % "bcpkix-jdk15on"  % "1.61" % Optional,
      "org.bouncycastle"  % "bcmail-jdk15on"  % "1.61" % Optional,
      "org.scalameta"     %% "munit"          % "0.7.29" % Test,
      "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
lazy val it = (project in file("integration-tests"))
  .settings(commonSettings ++ cFlags)
  .settings(
    name := "courier-integration-tests",
    libraryDependencies ++= Seq(
      "org.scalameta"     %% "munit"          % "0.7.29" % Test,
    ),
    testFrameworks += new TestFramework("munit.Framework")
  ).dependsOn(courier)
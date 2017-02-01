organization := "ch.lightshed"

name := "courier"

version := "0.2.0-SNAPSHOT"

description := "deliver electronic mail with scala"

libraryDependencies ++= Seq(
  "javax.mail"              %  "mail"            % "1.4.7",
  "javax.activation"        %  "activation"      % "1.1.1",
  "org.specs2"              %% "specs2-core"     % "3.8.7"  % "test",
  "org.jvnet.mock-javamail" % "mock-javamail"    % "1.9"    % "test"
)

licenses := Seq(
  ("MIT", url(s"https://github.com/softprops/${name.value}/blob/${version.value}/LICENSE")))

homepage := Some(url(s"https://github.com/softprops/${name.value}/#readme"))

crossScalaVersions := Seq("2.10.4", "2.11.8", "2.12.0")

scalaVersion := crossScalaVersions.value.last

scalacOptions in Test ++= Seq("-Yrangepos")

bintrayOrganization := Some("lightshed")

bintrayReleaseOnPublish in ThisBuild := false

bintrayPackageLabels := Seq("email", "mail", "javamail")

pomExtra := (
  <scm>
    <url>git@github.com:softprops/courier.git</url>
    <connection>scm:git:git@github.com:softprops/courier.git</connection>
  </scm>
  <developers>
    <developer>
      <id>softprops</id>
      <name>Doug Tangren</name>
      <url>https://github.com/softprops</url>
    </developer>
  </developers>)

organization := "me.lessis"

name := "courier"

version := "0.1.4-SNAPSHOT"

description := "deliver electronic mail with scala"

libraryDependencies ++= Seq(
  "javax.mail"        % "mail"        % "1.4.7",
  "javax.activation"  % "activation"  % "1.1.1"
)

licenses := Seq(
  ("MIT", url(s"https://github.com/softprops/${name.value}/blob/${version.value}/LICENSE")))

homepage := Some(url(s"https://github.com/softprops/${name.value}/#readme"))

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalaVersion := crossScalaVersions.value.last

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

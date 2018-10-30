organization := "ch.lightshed"

name := "courier"

version := "0.2.0"

description := "deliver electronic mail with scala"

libraryDependencies ++= Seq(
  "javax.mail"        % "mail"            % "1.4.7",
  "javax.activation"  % "activation"      % "1.1.1",
  "org.bouncycastle"  % "bcpkix-jdk15on"  % "1.60",
  "org.bouncycastle"  % "bcmail-jdk15on"  % "1.60"
)

licenses := Seq(
  ("MIT", url(s"https://github.com/softprops/${name.value}/blob/${version.value}/LICENSE")))

homepage := Some(url(s"https://github.com/softprops/${name.value}/#readme"))

crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.7")

scalaVersion := crossScalaVersions.value.last

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

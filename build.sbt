organization := "me.lessis"

name := "courier"

version := "0.1.3"

description := "deliver electronic mail with scala"

libraryDependencies ++= Seq(
  "javax.mail"        % "mail"        % "1.4.7",
  "javax.activation"  % "activation"  % "1.1.1"
)

licenses := Seq(
  ("MIT", url("https://github.com/softprops/%s/blob/%s/LICENSE"
              .format(name.value,version.value))))

homepage := Some(url("https://github.com/softprops/%s/#readme".format(name.value)))

crossScalaVersions := Seq("2.9.3", "2.10.4", "2.11.0")

scalaVersion := crossScalaVersions.value.last

seq(bintraySettings:_*)

bintray.Keys.packageLabels in bintray.Keys.bintray := Seq("email", "mail")

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("email", "mail")

(externalResolvers in LsKeys.lsync) := (resolvers in bintray.Keys.bintray).value

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

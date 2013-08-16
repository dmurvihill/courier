organization := "me.lessis"

name := "courier"

version := "0.1.0"

libraryDependencies ++= Seq(
  "javax.mail"        % "mail"        % "1.4.7",
  "javax.activation"  % "activation"  % "1.1.1"
)

licenses := Seq(
  ("MIT", url("https://github.com/softprops/%s/blob/%s/LICENSE"
              .format(name.value,version.value))))

homepage := Some(url("https://github.com/softprops/%s/#readme".format(name.value)))

scalaVersion := "2.9.3"

crossScalaVersions := Seq("2.9.3", "2.10.2")

seq(bintraySettings:_*)

bintray.Keys.packageLabels in bintray.Keys.bintray := Seq("email", "mail")

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("email", "mail")

(externalResolvers in LsKeys.lsync) := (resolvers in bintray.Keys.bintray).value

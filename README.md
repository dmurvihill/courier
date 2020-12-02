# courier

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.daddykotex/courier_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.daddykotex/courier_2.13)

deliver electronic mail with scala from the [future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## install

Via the copy and paste method

```scala
libraryDependencies += "com.github.daddykotex" %% "courier" % "3.0.0-M2"
```

Note: Scala3 (or Dotty) is supported.
- `3.0.0-RC1` for dotty: `0.27.0-RC1`
- `3.0.0-M1`  for dotty: `3.0.0-M1`
- `3.0.0-M2`  for dotty: `3.0.0-M2`

## usage

deliver electronic mail via gmail

```scala
import courier._, Defaults._
val mailer = Mailer("smtp.gmail.com", 587)
               .auth(true)
               .as("you@gmail.com", "p@$$w3rd")
               .startTls(true)()
mailer(Envelope.from("you" `@` "gmail.com")
        .to("mom" `@` "gmail.com")
        .cc("dad" `@` "gmail.com")
        .subject("miss you")
        .content(Text("hi mom"))).onSuccess {
          case _ => println("message delivered")
        }

mailer(Envelope.from("you" `@` "work.com")
         .to("boss" `@` "work.com")
         .subject("tps report")
         .content(Multipart()
           .attach(new java.io.File("tps.xls"))
           .html("<html><body><h1>IT'S IMPORTANT</h1></body></html>")))
           .onSuccess {
             case _ => println("delivered report")
           }
```

If using SSL/TLS instead of STARTTLS, substitute `.startTls(true)` with `.ssl(true)` when setting up the `Mailer`.

### S/MIME
Courier supports sending S/MIME signed email through its optional dependencies on the Bouncycastle cryptography libraries. It does not yet support sending encrypted email.

Make sure the Mailer is instantiated with a signer, and then wrap your message in a Signed() object.

```scala
import courier._
import java.security.cert.X509Certificate
import java.security.PrivateKey

val certificate: X509Certificate = ???
val privateKey: PrivateKey = ???
val trustChain: Set[X509Certificate] = ???

val signer = Signer(privateKey, certificate, trustChain)
val mailer = Mailer("smtp.gmail.com", 587)
               .auth(true)
               .as("you@gmail.com", "p@$$w3rd")
               .withSigner(signer)
               .startTtls(true)()
val envelope = Envelope
        .from("mr_pink" `@` "gmail.com")
        .to("mr_white" `@` "gmail.com")
        .subject("the jewelry store")
        .content(Signed(Text("For all I know, you're the rat.")))

mailer(envelope)
```


## testing

Since courier is based on JavaMail, you can use [Mock JavaMail](https://java.net/projects/mock-javamail) to execute your tests. Simply add the following to your `build.sbt`:

```scala
libraryDependencies += "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"
```

With this library, you should, given a little bit of boilerplate, be able to set a test against a mocked Mailbox. This repo contains [an example](src/test/scala/mailspec.scala).

(C) Doug Tangren (softprops) and others, 2013-2018

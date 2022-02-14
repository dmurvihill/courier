# courier

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.daddykotex/courier_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.daddykotex/courier_2.13)

deliver electronic mail with scala from the [future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## install

Via the copy and paste method

```sbt
libraryDependencies += "com.github.daddykotex" %% "courier" % "3.0.1"
```

3.1.0 supports scala 2.11 to 3.1

Note: Scala3 (or Dotty) is supported.

- `3.0.0-RC1` for dotty: `0.27.0-RC1`
- `3.0.0-M1` for dotty: `3.0.0-M1`
- `3.0.0-M2` for dotty: `3.0.0-M2`
- `3.0.1` scala 3: `3.0.1`
- `3.1.0` scala 3: `3.1.0`

## usage

deliver electronic mail via gmail

```scala
import courier._, Defaults._
import scala.util._
val mailer = Mailer("smtp.gmail.com", 587)
               .auth(true)
               .as("you@gmail.com", "p@$$w3rd")
               .startTls(true)()
mailer(Envelope.from("you" `@` "gmail.com")
        .to("mom" `@` "gmail.com")
        .cc("dad" `@` "gmail.com")
        .subject("miss you")
        .content(Text("hi mom"))).onComplete {
          case Success(_) => println("message delivered")
          case Failure(_) => println("delivery failed")
        }

mailer(Envelope.from("you" `@` "work.com")
         .to("boss" `@` "work.com")
         .subject("tps report")
         .content(Multipart()
           .attach(new java.io.File("tps.xls"))
           .html("<html><body><h1>IT'S IMPORTANT</h1></body></html>")))
           .onComplete {
             case Success(_) => println("delivered report")
             case Failure(_) => println("delivery failed")
           }
```

If using SSL/TLS instead of STARTTLS, substitute `.startTls(true)` with `.ssl(true)` when setting up the `Mailer`.

### Retry

If you need to retry email sending, you can include `courier-retry`. First, add the dependencies:

```sbt
libraryDependencies += "com.github.daddykotex" %% "courier-retry" % "3.0.1"
```

Then, you need two things:

  * an implicit `RetryConfig` in scope
  * `import courier.Retry._`

When you have this, you can replace your calls to `Mailer.apply` by `Mailer.sendWithRetry`. The following is a rewrite of the example above, that retry mail sending in case of failure using the default policy.

```scala
import courier._, Defaults._, Retry._
import scala.util._

implicit val retryConfig: RetryConfig = RetryConfig.Default
val mailer = Mailer("smtp.gmail.com", 587)
               .auth(true)
               .as("you@gmail.com", "p@$$w3rd")
               .startTls(true)()

mailer.sendWithRetry(Envelope.from("you" `@` "gmail.com")
        .to("mom" `@` "gmail.com")
        .cc("dad" `@` "gmail.com")
        .subject("miss you")
        .content(Text("hi mom"))).onComplete {
          case Success(_) => println("message delivered")
          case Failure(_) => println("delivery failed")
        }
```

#### Default retry policy

To implement the retrying behaviour, we use https://github.com/softwaremill/retry. The default retry policy in `courier` retries when a specific set of exception occurs, currently:

* com.sun.mail.util.MailConnectException

Defining your own retry policy can be done in various ways:

```scala
implicit val a: RetryConfig = RetryConfig.retryForExceptions(RetryConfig.DefaultPolicy, List(classOf[SendFailedException])) // retrying only when a SendFailedException is thrown

implicit val a: RetryConfig = {
  val pause = retry.Pause(3, 10.seconds)
  val when = retry.When {
    case NonFatal(ex: SendFailedException) if ex.getInvalidAddresses().nonEmpty => pause
  }
  RetryConfig(when, retry.Success.always) // pause for 10 second between retry, maximum 3 times, and only if the partial function above applies
}
```

See `retry` documentation for more information.

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

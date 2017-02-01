# courier

deliver electronic mail with scala from the [future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## install

Via the copy and paste method

```scala
resolvers += "lightshed-maven" at "http://dl.bintray.com/content/lightshed/maven"

libraryDependencies += "ch.lightshed" %% "courier" % "0.1.4"
```

Note. If you are a [bintray-sbt](https://github.com/softprops/bintray-sbt#readme) user you can optionally specify the resolver as

```scala
resolvers += Resolver.bintrayRepo("lightshed", "maven")
```

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

## configuration

The default behaviour the mail session can be configured via system properties. 
For available properties, see [JavaMail docs](https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html#properties).

## Upgrade notes for 0.1.x users
* Package has been renamed from `courier` to `ch.lightshed.courier`

## testing

Since courier is based on JavaMail, you can use [Mock JavaMail](https://java.net/projects/mock-javamail) to execute your tests.
[Here](https://weblogs.java.net/blog/2007/04/26/introducing-mock-javamail-project) is an excellent article on using Mock JavaMail.

See [MailerSpec.scala](src/test/scala/ch/lightshed/courier/MailerSpec.scala) for an example.

Doug Tangren (softprops) 2013

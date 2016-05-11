# courier

deliver electronic mail with scala from the [future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## install

Via the copy and paste method

```scala
resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

libraryDependencies += "me.lessis" %% "courier" % "0.1.3"
```

Via [a more civilized method](https://github.com/softprops/ls#readme) which will do the same without all the manual work.

    > ls-install courier
    
Note. If you are a [bintray-sbt](https://github.com/softprops/bintray-sbt#readme) user you can optionally specify the resolver as
    
```scala
resolvers += bintray.Opts.resolver.repo("softprops", "maven")
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

## testing

Since courier is based on JavaMail, you can use [Mock JavaMail](https://java.net/projects/mock-javamail) to execute your tests. Simply add the following to your `build.sbt`:

```scala
libraryDependencies += "org.jvnet.mock-javamail" % "mock-javamail" % "1.9" % "test"
```

Having this in your test dependencies will automatically enable Mock JavaMail during tests. You can then test for email sends, etc.

```scala
import courier._
import org.specs2.mutable.Specification
import scala.concurrent.duration._

// Need NoTimeConversions to prevent conflict with scala.concurrent.duration._
class MailSpec extends Specification with NoTimeConversions {
  "the mailer" should {
  	"send an email" in {
  	  val mailer = Mailer("localhost", 25)
  	  val future = mailer(Envelope.from("someone@example.com")
          	.to("mom@gmail.com")
          	.cc("dad@gmail.com")
          	.subject("miss you")
          	.content(Text("hi mom")))
          	
          Await.ready(future, 5.seconds)
          val momsInbox = Mailbox.get("mom@gmail.com")
          momsInbox.size === 1
          val momsMsg = momsInbox.get(0)
          momsMsg.getContent === "hi mom"
          momsMsg.getSubject === "miss you"
        }       	  	}  }
}        
```

[Here](https://weblogs.java.net/blog/2007/04/26/introducing-mock-javamail-project) is an excellent article on using Mock JavaMail.

Doug Tangren (softprops) 2013

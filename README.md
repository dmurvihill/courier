# courier

deliver electronic mail with scala from the [future](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future)

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## install

Via the copy and paste method

```scala
resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"

libraryDependencies += "me.lessis" %% "courier" % "0.1.0"
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
               .startTtls(true)()
                     
mailer(Envelope.from("you" `@` "gmail.com")
        .to("mom" `@` "gmail.com")
        .subject("miss you")
        .content(Text("hi mom"))).onSuccess {
          case _ => println("message delivered")
        }

mailer(Envelope.from("you" `@` "work.com")
         .to("boss" `@` "work.com")
         .subject("tps report")
         .content(Multipart()
           .attach("tps.xls")
           .html("<html><body><h1>IT'S IMPORTANT</h1></body></html>")))
           .onSuccess {
             case _ => println("delivered report")
           }
```

Doug Tangren (softprops) 2013

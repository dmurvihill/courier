# courier

send electronic mail with scala

![courier](http://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Courrier.jpg/337px-Courrier.jpg)

## usage

send electronic mail via gmail

```scala
import courier._, Defaults._
val mailer = Mailer("smtp.gmail.com", 587)
               .auth(true)
               .as("you@gmail.com", "p@$$w3rd")
               .startTtls(true)()
                     
mailer(Envelope("you" `@` "gmail.com")
        .to("mom" `@` "gmail.com")
        .subject("miss you")
        .content(Text("hi mom")))                     
```

Doug Tangren (softprops) 2013

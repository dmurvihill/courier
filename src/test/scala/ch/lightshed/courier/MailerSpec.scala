package ch.lightshed.courier

import org.jvnet.mock_javamail.Mailbox
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class MailerSpec extends Specification {
  "the mailer" should {
    "send an email" in {
      val mailer = Mailer("localhost", 25)()
      val future = mailer(Envelope.from("someone@example.com".addr)
        .to("mom@gmail.com".addr)
        .cc("dad@gmail.com".addr)
        .subject("miss you")
        .content(Text("hi mom")))

      Await.ready(future, 10.seconds)

      val momsInbox = Mailbox.get("mom@gmail.com")
      momsInbox.size === 1
      val momsMsg = momsInbox.get(0)
      momsMsg.getContent === "hi mom"
      momsMsg.getSubject === "miss you"
    }
  }
}

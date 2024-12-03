package courier

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class MailSpec extends munit.FunSuite {
  // create a gmail app password https://myaccount.google.com/apppasswords
  test("the mailer should send an email") {
    assume(sys.env("CI").isEmpty(), "This test is meant to be ran locally.")
    val email = sys.env("IT_EMAIL")
    val password = sys.env("IT_PASSWORD")
    val mailer = Mailer("smtp.gmail.com", 587)
                .auth(true)
                .as(email, password)
                .startTls(true)()
    val mId = Await.result(mailer(Envelope.from(email.addr)
            .to(email.addr)
            .subject("miss you")
            .content(Text("hi mom"))), 10.seconds)
    assert(mId.nonEmpty, "Message-ID should be set by the Transport.send call")
  }
}
package courier

import java.util.Properties

import javax.mail.Provider
import org.jvnet.mock_javamail.{Mailbox, MockTransport}
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class MockedSMTPProvider extends Provider(Provider.Type.TRANSPORT, "mocked", classOf[MockTransport].getName, "Mock", null)

class MailSpec extends WordSpec {
  private val mockedSession = javax.mail.Session.getDefaultInstance(new Properties() {{
    put("mail.transport.protocol.rfc822", "mocked")
  }})
  mockedSession.setProvider(new MockedSMTPProvider)

  "the mailer" should {
    "send an email" in {
      val mailer = Mailer(mockedSession)
      val future = mailer(Envelope.from("someone@example.com".addr)
            .to("mom@gmail.com".addr)
            .cc("dad@gmail.com".addr)
            .subject("miss you")
            .content(Text("hi mom")))

      Await.ready(future, 5.seconds)
      val momsInbox = Mailbox.get("mom@gmail.com")
      momsInbox.size === 1
      val momsMsg = momsInbox.get(0)
      momsMsg.getContent === "hi mom"
      momsMsg.getSubject === "miss you"
    }
  }
}
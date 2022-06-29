package courier

import javax.mail.{Session => MailSession}
import java.util.Properties
import javax.mail.internet.MimeMessage
import scala.concurrent.ExecutionContext

object Defaults {
  val session = MailSession.getDefaultInstance(new Properties())

  val mimeMessageFactory: MailSession => MimeMessage = new MimeMessage(_)

  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
}

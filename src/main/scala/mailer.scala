package courier

import javax.mail.{ Message, Session, Transport }
import javax.mail.internet.MimeMessage
import scala.concurrent.{ ExecutionContext, future }

case class Mailer(
  _session: Session = Defaults.session) {
  def session = Sessions.Builder(this)

  def apply(e: Envelope)(implicit ec: ExecutionContext) = {
    val msg = new MimeMessage(_session) {
      e.subject.map(setSubject(_))
      setFrom(e.from)
      e.recipients.foreach(addRecipient(Message.RecipientType.TO, _))
      e.contents.map {
        case Text(txt) => setText(txt)
        case mp @ Multiparts(_) => setContent(mp.parts)
      }
    }
    future {
      Transport.send(msg)
    }
  }
}

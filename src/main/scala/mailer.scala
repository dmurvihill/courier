package courier

import javax.mail.internet.MimeMessage
import javax.mail.{Message, Transport, Session => MailSession}

import scala.concurrent.{ExecutionContext, Future}

object Mailer {
  def apply(host: String, port: Int): Session.Builder =
    Mailer().session.host(host).port(port)
}

case class Mailer(_session: MailSession = Defaults.session, signer: Option[Signer] = None) {
  def session = Session.Builder(this)

  def apply(e: Envelope)(implicit ec: ExecutionContext): Future[Unit] = {
    val msg = new MimeMessage(_session) {
      e.subject.foreach {
        case (subject, Some(charset)) => setSubject(subject, charset.name())
        case (subject, None) => setSubject(subject)
      }
      setFrom(e.from)
      e.to.foreach(addRecipient(Message.RecipientType.TO, _))
      e.cc.foreach(addRecipient(Message.RecipientType.CC, _))
      e.bcc.foreach(addRecipient(Message.RecipientType.BCC, _))
      e.replyTo.foreach(a => setReplyTo(Array(a)))
      e.headers.foreach(h => addHeader(h._1, h._2))
      e.contents match {
        case Text(txt, charset) => setText(txt, charset.displayName)
        case mp: Multipart => setContent(mp.parts)
        case Signed(body) =>
          if(signer.isDefined) setContent(signer.get.sign(body))
          else throw new IllegalArgumentException("No signer defined, cannot sign!")
      }
    }
    Future {
      Transport.send(msg)
    }
  }
}



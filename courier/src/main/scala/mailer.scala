package courier

import javax.mail.internet.MimeMessage
import javax.mail.{Message, Transport, Session => MailSession}

import scala.concurrent.{ExecutionContext, Future}

object Mailer {
  def apply(host: String, port: Int): Session.Builder =
    Mailer().session.host(host).port(port)
}

case class Mailer(_session: MailSession = Defaults.session,
                  signer: Option[Signer] = None,
                  mimeMessageFactory: MailSession => MimeMessage = Defaults.mimeMessageFactory) {
  def session = Session.Builder(this)

  def apply(e: Envelope)(implicit ec: ExecutionContext): Future[Unit] = {
    val msg = mimeMessageFactory(_session)

    e.subject.foreach {
      case (subject, Some(charset)) => msg.setSubject(subject, charset.name())
      case (subject, None) => msg.setSubject(subject)
    }
    msg.setFrom(e.from)
    e.to.foreach(msg.addRecipient(Message.RecipientType.TO, _))
    e.cc.foreach(msg.addRecipient(Message.RecipientType.CC, _))
    e.bcc.foreach(msg.addRecipient(Message.RecipientType.BCC, _))
    e.replyTo.foreach(a => msg.setReplyTo(Array(a)))
    e.headers.foreach(h => msg.addHeader(h._1, h._2))
    e.contents match {
      case Text(txt, charset) => msg.setText(txt, charset.displayName)
      case mp: Multipart => msg.setContent(mp.parts)
      case Signed(body) =>
        if (signer.isDefined) msg.setContent(signer.get.sign(body))
        else throw new IllegalArgumentException("No signer defined, cannot sign!")
    }

    Future {
      Transport.send(msg)
    }
  }
}



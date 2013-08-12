package courier

import javax.mail.internet.{ MimeBodyPart, MimeMultipart }

sealed trait Content

case class Text(body: String) extends Content

case class Multiparts(_parts: Seq[MimeBodyPart]) extends Content {
  def add(
    bytes: Array[Byte],
    mimetype: String,
    name: Option[String] = None) =
    Multiparts(_parts :+ new MimeBodyPart {
      setContent(bytes, mimetype)
      name.map(setFileName(_))
    })
  def parts =
    new MimeMultipart() {
      _parts.foreach(addBodyPart(_))
    }
}

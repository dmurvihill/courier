package courier

import java.io.File
import java.nio.charset.Charset

import javax.activation.{DataHandler, FileDataSource}
import javax.mail.internet.{MimeBodyPart, MimeMultipart}
import javax.mail.util.ByteArrayDataSource

sealed trait Content

case class Text(body: String, charset: Charset = Charset.defaultCharset) extends Content

case class Signed(body: Content) extends Content

case class Multipart(_parts: Seq[MimeBodyPart] = Seq.empty[MimeBodyPart], subtype: String = "mixed") extends Content {
  def add(part: MimeBodyPart): Multipart =
    this.copy(_parts = _parts :+ part)

  def add(
    bytes: Array[Byte],
    mimetype: String,
    name: Option[String] = None,
    disposition: Option[String] = None,
    description: Option[String] = None): Multipart =
    add(new MimeBodyPart {
      setContent(bytes, mimetype)
      disposition.foreach(setDisposition)
      description.foreach(setDescription)
      name.foreach(setFileName)
    })

  def text(str: String) =
    add(new MimeBodyPart {
      setContent(str, "text/plain")
    })

  def html(str: String) =
    add(new MimeBodyPart {
      setContent(str, "text/html")
    })

  def attach(file: File, name: Option[String] = None) =
    add(new MimeBodyPart {
      setDataHandler(new DataHandler(new FileDataSource(file)))
      setFileName(name.getOrElse(file.getName))
    })

  def attachBytes(bytes: Array[Byte], name: String, mimeType: String) =
    add(new MimeBodyPart {
      setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, mimeType)))
      setFileName(name)
    })

  def parts =
    new MimeMultipart(subtype) {
      _parts.foreach(addBodyPart(_))
    }
}

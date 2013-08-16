package courier

import javax.activation.{ DataHandler, FileDataSource }
import javax.mail.internet.{ MimeBodyPart, MimeMultipart }
import java.io.File
import java.nio.charset.Charset

sealed trait Content

case class Text(body: String, charset: Charset = Charset.defaultCharset)
  extends Content

case class Multipart(
  _parts: Seq[MimeBodyPart] = Seq.empty[MimeBodyPart])
  extends Content {
  def add(part: MimeBodyPart): Multipart =
    Multipart(_parts :+ part)
  def add(
    bytes: Array[Byte],
    mimetype: String,
    name: Option[String] = None,
    disposition: Option[String] = None,
    description: Option[String] = None): Multipart =
    add(new MimeBodyPart {
      setContent(bytes, mimetype)
      disposition.map(setDisposition(_))
      description.map(setDescription(_))
      name.map(setFileName(_))
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

  def parts =
    new MimeMultipart() {
      _parts.foreach(addBodyPart(_))
    }
}

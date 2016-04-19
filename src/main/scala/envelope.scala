package courier

import java.nio.charset.Charset
import javax.mail.internet.InternetAddress

object Envelope {
  def from(addr: InternetAddress) =
    Envelope(addr)
}

case class Envelope(
  from: InternetAddress,
  _subject: Option[(String, Charset)] = None,
  _to: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _cc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _bcc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _replyTo: Option[InternetAddress] = None,
  _replyToAll: Option[Boolean] = None,
  _headers: Seq[(String, String)] = Seq.empty[(String, String)],
  _content: Content = Text("")) {

  def subject(s: String) = copy(_subject = Some(s, Charset.defaultCharset()))
  def subject(s: String, ch: Charset) = copy(_subject = Some(s, ch))
  def to(addrs: InternetAddress*) = copy(_to = _to ++ addrs)
  def cc(addrs: InternetAddress*) = copy(_cc = _cc ++ addrs)
  def bcc(addrs: InternetAddress*) = copy(_bcc = _bcc ++ addrs)
  def replyTo(addr: InternetAddress) = copy(_replyTo = Some(addr))
  def replyAll = copy(_replyToAll = Some(true))
  def headers(hdrs: (String, String)*) = copy(_headers = _headers ++ hdrs)
  def content(c: Content) = copy(_content = c)

  def contents = _content
  def subject = _subject
  @deprecated("use `to` instead", "0.1.1")
  def recipients = _to
  def to = _to
  def cc = _cc
  def bcc = _bcc
  def headers = _headers
}

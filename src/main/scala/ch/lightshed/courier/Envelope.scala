package ch.lightshed.courier

import java.nio.charset.Charset
import javax.mail.internet.InternetAddress

object Envelope {
  def from(addr: InternetAddress): Envelope = Envelope(addr)
}

case class Envelope(
  from: InternetAddress,
  _subject: Option[(String, Option[Charset])] = None,
  _to: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _cc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _bcc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _replyTo: Option[InternetAddress] = None,
  _replyToAll: Option[Boolean] = None,
  _headers: Seq[(String, String)] = Seq.empty[(String, String)],
  _content: Content = Text("")) {

  def subject(s: String): Envelope = copy(_subject = Some(s, None))
  def subject(s: String, ch: Charset): Envelope = copy(_subject = Some(s, Some(ch)))
  def to(addrs: InternetAddress*): Envelope = copy(_to = _to ++ addrs)
  def cc(addrs: InternetAddress*): Envelope = copy(_cc = _cc ++ addrs)
  def bcc(addrs: InternetAddress*): Envelope = copy(_bcc = _bcc ++ addrs)
  def replyTo(addr: InternetAddress): Envelope = copy(_replyTo = Some(addr))
  def replyAll: Envelope = copy(_replyToAll = Some(true))
  def headers(hdrs: (String, String)*): Envelope = copy(_headers = _headers ++ hdrs)
  def content(c: Content): Envelope = copy(_content = c)

  def contents: Content = _content
  def subject: Option[(String, Option[Charset])] = _subject
  @deprecated("use `to` instead", "0.1.1")
  def recipients: Seq[InternetAddress] = _to
  def to: Seq[InternetAddress] = _to
  def cc: Seq[InternetAddress] = _cc
  def bcc: Seq[InternetAddress] = _bcc
  def replyTo: Option[InternetAddress] = _replyTo
  def headers: Seq[(String, String)] = _headers
}

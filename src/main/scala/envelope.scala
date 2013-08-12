package courier

import javax.mail.internet.InternetAddress

case class Envelope(
  from: InternetAddress,
  _to: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _cc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _bcc: Seq[InternetAddress] = Seq.empty[InternetAddress],
  _replyTo: Option[InternetAddress] = None,
  _headers: Seq[(String, String)] = Seq.empty[(String, String)],
  _content: Option[Content] = None) {
  def to(addrs: InternetAddress*) = copy(_to = _to ++ addrs)
  def cc(addrs: InternetAddress*) = copy(_cc = _cc ++ addrs)
  def bcc(addrs: InternetAddress*) = copy(_bcc = _bcc ++ addrs)
  def replyTo(addr: InternetAddress) = copy(_replyTo = Some(addr))
  def headers(hdrs: (String, String)*) = copy(_headers = _headers ++ hdrs)
  def content(c: Content) = copy(_content = Some(c))

  def contents = _content
  def recipients = _to
}

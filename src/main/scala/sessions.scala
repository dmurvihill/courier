package courier

import javax.mail.{ PasswordAuthentication, Session }
import java.util.Properties

object Sessions {
  case class Builder(
    mailer: Mailer,
    _auth: Option[Boolean] = None,
    _startTtls: Option[Boolean] = None,
    _host: Option[String] = None,
    _port: Option[Int] = None,
    _creds: Option[(String, String)] = None) {
    def auth(a: Boolean) = copy(_auth= Some(a))
    def startTtls(s: Boolean) = copy(_startTtls = Some(s))
    def host(h: String) = copy(_host = Some(h))
    def port(p: Int) = copy(_port = Some(p))
    def credentials(user: String, pass: String) =
      copy(_creds = Some((user, pass)))
    def apply() =
      mailer.copy(_session = Session.getInstance(new Properties {
        _auth.map(a => put("mail.smtp.auth", a.toString))
        _startTtls.map(s => put("mail.smtp.starttls.enable", s.toString))
        _host.map(put("mail.smtp.host", _))
        _port.map(p => put("mail.smtp.port", p.toString))
      }, _creds.map {
        case (user, pass) =>
          new javax.mail.Authenticator {
            protected override def getPasswordAuthentication() =
              new PasswordAuthentication(user, pass)
          }
      }
      .getOrElse(null)))
  }
}

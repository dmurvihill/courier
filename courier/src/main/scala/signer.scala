package courier

import java.security.PrivateKey
import java.security.cert.X509Certificate

import javax.mail.internet.{MimeBodyPart, MimeMultipart}
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder
import org.bouncycastle.mail.smime.SMIMESignedGenerator

case class Signer(signingKey: PrivateKey, signingCert: X509Certificate, certStore: Set[X509Certificate]) {
  private val gen: SMIMESignedGenerator = new SMIMESignedGenerator()
  private val certs = new JcaCertStore(Compat.asJava(certStore + signingCert))
  gen.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().build("SHA256withRSA", signingKey, signingCert))
  gen.addCertificates(certs)

  def sign(content: Content): MimeMultipart = {
    val preppedContent = getContentToSign(content)
    gen.generate(preppedContent)
  }

  private def getContentToSign(c: Content): MimeBodyPart = {
    val bp = new MimeBodyPart()
    c match {
      case mp: Multipart => bp.setContent(mp.parts)
      case Text(txt, charset) => bp.setContent(txt, s"text/plain; charset=$charset")
      case _: Signed => throw new UnsupportedOperationException("Nested signed entities not supported")
    }
    bp
  }
}

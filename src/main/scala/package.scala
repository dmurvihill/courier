import javax.mail.internet.InternetAddress
import scala.language.implicitConversions

/** An agreeable email interface for scala. */
package object courier {

  implicit def str2Address(name: String) = new InternetAddress(name)

  implicit class addr(name: String){
    def `@`(domain: String): InternetAddress = new InternetAddress(s"$name@$domain")
    def at = `@` _
  }
}

import javax.mail.internet.InternetAddress

/** An agreeable email interface for scala. */
package object courier {

  implicit class Addr(val name: String) extends AnyVal {
    def `@`(host: String) = new InternetAddress("%s@%s" format(name, host))
    
    def at = `@` _

    /** In case whole string is email address already */
    def addr = new InternetAddress(name)
  }
}

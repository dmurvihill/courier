import javax.mail.internet.InternetAddress

/** An agreeable email interface for scala. */
package object courier {

  // change to implicit class when dropping 2.9.3
  implicit def addr(name: String) =/*extends AnyVal*/ new {
    def `@`(host: String) = new InternetAddress("%s@%s" format(name, host))
    
    def at = `@` _

    /** In case whole string is email address already */
    def addr = new InternetAddress(name)
  }
}

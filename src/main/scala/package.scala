import javax.mail.internet.InternetAddress

/** An agreeable email interface for scala. */
package object courier {
  implicit class Addr(name: String) {
    def `@`(host: String) = new InternetAddress("%s@%s" format(name, host))
    def at(host: String) = new InternetAddress("%s@%s" format(name, host))
  }
}

package ch.lightshed

import javax.mail.internet.InternetAddress

/** An agreeable email interface for scala. */
package object courier {

  implicit class RichInternetAddress(name: String){
    def `@`(domain: String): InternetAddress = new InternetAddress(s"$name@$domain")
    def at: String => InternetAddress = `@`
    /** In case whole string is email address already */
    def addr: InternetAddress = new InternetAddress(name)
  }
}

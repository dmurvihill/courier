package ch.lightshed.courier

import javax.mail.internet.InternetAddress

import org.specs2.mutable.Specification

class RichInternetAddressSpec extends Specification  {
  "The implicit RichInternetAddress" should {
    "support `@` operator on strings to create a new InternetAddress" in {
      "john.doe" `@` "example.com" must beEqualTo(new InternetAddress("john.doe@example.com"))
    }

    "support 'at' operator on strings to create a new InternetAddress" in {
      "john.doe" at "example.com" must beEqualTo(new InternetAddress("john.doe@example.com"))
    }

    "support 'addr' operator on strings to create a new InternetAddress" in {
      "john.doe@example.com".addr must beEqualTo(new InternetAddress("john.doe@example.com"))
    }
  }
}

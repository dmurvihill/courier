package ch.lightshed.courier

import javax.mail.{Session => MailSession}
import java.util.Properties
import scala.concurrent.ExecutionContext

object Defaults {
  val session: MailSession = MailSession.getDefaultInstance(new Properties())
  
  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
}

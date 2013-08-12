package courier

import javax.mail.Session
import java.util.Properties
import scala.concurrent.ExecutionContext

object Defaults {
  val session: Session =
    Session.getDefaultInstance(new Properties(), null)
  implicit val executionContext: ExecutionContext =
    ExecutionContext.Implicits.global
}

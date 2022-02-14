package courier

import com.sun.mail.util.MailConnectException

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.control.NonFatal
import javax.mail.SendFailedException

case class RetryConfig(
    policy: retry.Policy,
    success: retry.Success[Unit]
)
object RetryConfig {

  val DefaultPolicy = retry.Backoff(2, 1.second)

  val Default = retryForExceptions(DefaultPolicy, List(classOf[MailConnectException]))

  /**
    * Use the given policy if an exception occurs and the exception class
    * is part of the given list of exception classes.
    *
    */
  def retryForExceptions(p: retry.Policy, retryOn: List[Class[_ <: Throwable]]): RetryConfig = RetryConfig(
    retry.When { case NonFatal(ex: Throwable) if retryOn.exists(_ == ex.getClass()) => p },
    retry.Success.always
  )
}

object Retry {
  implicit class RetrySend(mailer: Mailer) {
    def sendWithRetry(mail: Envelope)(implicit retryConfig: RetryConfig, ec: ExecutionContext): Future[Unit] = {
      implicit val success: retry.Success[Unit] = retryConfig.success
      retryConfig.policy { mailer(mail) }
    }
  }
}

package courier

object Compat {
  def asJava[T](set: Set[T]): java.util.Set[T] = {
    import scala.jdk.CollectionConverters._
    set.asJava
  }
}

package courier

object Compat {
  def asJava[T](set: Set[T]): java.util.Set[T] = {
    import collection.JavaConverters._
    set.asJava
  }
}
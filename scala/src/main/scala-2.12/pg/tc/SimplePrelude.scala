package tc

object SimplePrelude {

  def id[A](a: A): A = a

  def const[A,B](a: A)(b: B): A = a

  def flip[A,B,C](f: A => B => C): B => A => C =
    b => a => f(a)(b)
}

package tcs

object Usage {
  import Maybe._
  import Functor.ops._
  import Applicative.ops._

  val x: Maybe[Int] = Just(3)
  val y: Maybe[Int] = Just(13)

  val f: Int => String =
    x => s"$x + 3"

  val ff: Maybe[Int => String] = Just(f)

  def g(x: Int)(y: Int): Int = x + y

  val gg: Maybe[Int => Int => Int] = Just(g)
}

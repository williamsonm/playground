import cats._, cats.implicits._
import cats.data.Nested

object compose {

  type LL[A] = List[List[A]]
  val ml: Monad[List] = Monad[List]

  val xs: LL[Int] = List(List(1,2),List(3,4))
  val nl = Nested(xs)

  val result = nl.map(_+1)
}

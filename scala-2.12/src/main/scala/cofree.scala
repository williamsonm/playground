import cats._
import cats.free._
import cats.implicits._

// final case class Cofree[F[_], A](head: A, tail: F[Cofree[F, A]])
// class Name[A](run: => A)

object CofreeX {
  // val fibs: Cofree[Eval, Int] = {
  //   def unfold(prev1: Int, prev2: Int): Cofree[Eval, Int] =
  //     Cofree(prev1 + prev2, Eval(unfold(prev2, prev1 + prev2)))
  //
  //   unfold(0, 1)
  // }

  // def append[F[_]: ApplicativePlus, A](c1: Cofree[F, A], c2: Cofree[F, A]): Cofree[F, A] =
  //   Cofree(c1.head, c1.tail.map(t => append(t, c2)) <+> c2.point[F])
}

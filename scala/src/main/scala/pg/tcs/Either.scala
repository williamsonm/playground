package tcs

sealed abstract class Xor[+A, +B] extends Product with Serializable
object Xor {
  final case class Left[+A](a: A) extends (A Xor Nothing)
  final case class Right[+B](b: B) extends (Nothing Xor B)

  implicit def xorInstance[L] = new Monad[Xor[L, ?]] {

    def map[A,B](fa: Xor[L,A])(f: A => B): Xor[L,B] = fa match {
      case Right(r) => Right(f(r))
      case Left(l) => Left(l)
    }

    def pure[A](a: A): Xor[L,A] =
      Xor.Right(a)

    def ap[A,B](fa: Xor[L,A])(f: Xor[L, A => B]): Xor[L,B] = (fa,f) match {
      case (r, Right(f)) => map(r)(f)
      case (_, Left(l)) => Left(l)
    }

    def flatMap[A,B](fa: Xor[L,A])(f: A => Xor[L,B]): Xor[L,B] = fa match {
      case Right(r) => f(r)
      case Left(l) => Left(l)
    }
  }
}

object TestXor {
	import Xor._
  def meh[F[_], A, B](fa: F[A])(f: A => B): F[B] = ???
  meh(new Right(23): Xor[Boolean, Int])(_ < 13)
  meh(new Left(true): Xor[Boolean, Int])(_ < 13)
}

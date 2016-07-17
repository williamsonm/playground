package tc

import SimplePrelude._

trait Functor[F[_]] {

  def fmap[A,B](f: A => B)(fa: F[A]): F[B]

  def as[A,B](a: A)(fb: F[B]): F[A] // = fmap(const(a))(fb)
}

object Functor {
    def apply[F[_]](implicit instance: Functor[F]): Functor[F] = instance
}

final class FunctorOps[F[_],A](val self: F[A])(implicit val F: Functor[F]) {
  final def fmap[B](f: A => B): F[B] = F.fmap(f)(self)
}

sealed trait Maybe[A]
final case class Just[A](value: A) extends Maybe[A]
final case class Nothing[A]() extends Maybe[A]

object Maybe {
  implicit def FunctorInstance = new Functor[Maybe] {
    def fmap[A,B](f: A => B)(fa: Maybe[A]): Maybe[B] = fa match {
      case Just(a) => Just(f(a))
      case Nothing() => Nothing[B]
    }

    def as[A,B](a: A)(fa: Maybe[B]): Maybe[A] = {
        def f(b: B): A = a
        fmap(f)(fa)
    }
  }
}

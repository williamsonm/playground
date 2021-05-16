package tcs

import simulacrum._
import tc.SimplePrelude._

@typeclass
trait Functor[F[_]] {
  def map[A,B](fa: F[A])(f: A => B): F[B]

  final def as[A,B](fb: F[B])(a: A): F[A] =
    map(fb)(_ => a)
}

@typeclass
trait Applicative[F[_]] extends Functor[F] {
  def pure[A](a: A): F[A]

  @op("<*>") def ap[A, B](fa: F[A])(f: F[A => B]): F[B]

  final def *>[A, B](fa: F[A])(fb: F[B]): F[B] =
    apply2(fa, fb)((_,b) => b)

  final def <*[A, B](fa: F[A])(fb: F[B]): F[A] =
    apply2(fa, fb)((a,_) => a)

  final def apply2[A, B, C](fa: => F[A], fb: => F[B])(f: (A, B) => C): F[C] =
    ap(fb)(map(fa)(f.curried))

  final def ap2[A,B,C](fa: F[A])(fb: F[B])(f: F[A => B => C]): F[C] =
    ap(fb)(ap(fa)(f))
}

@typeclass
trait Monad[F[_]] extends Applicative[F] {
  @op(">>=")
  def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]

  def >>[A,B](fa: F[A])(fb: F[B]): F[B] =
    flatMap(fa)(_ => fb)

  def ret[A](a: A): F[A] = pure(a)
}

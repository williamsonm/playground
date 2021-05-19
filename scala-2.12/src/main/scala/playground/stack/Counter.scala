package playground.stack

import cats._
import cats.free._

sealed trait CounterF[A]
case class Increment[A](a: A) extends CounterF[A]
case class Read[A](k: Int => A) extends CounterF[A]
case class Reset[A](a: A) extends CounterF[A]

object CounterF {
  implicit def functorCounterF: Functor[CounterF] = new Functor[CounterF] {
    def map[A, B](fa: CounterF[A])(f: A => B): CounterF[B] = fa match {
      case Increment(a) => Increment(f(a))
      case Read(k)      => Read (k andThen f)
      case Reset(a)     => Reset (f(a))
    }
  }
}

object Counter {
  type Counter[A] = Free[CounterF, A]

  // def liftFree[F[_], A](fa: F[A]): Free[F, A] =
  //   ???

  def increment: Counter[Unit] =
    Free.liftF(Increment(()))

  def read: Counter[Int] =
    Free.liftF(Read(s => s))

  def reset: Counter[Unit] =
    Free.liftF(Reset(()))

  def readAndReset: Counter[Int] =
    for {
      current <- read
      _ <- reset
    } yield current

  // def runCounter[S, A](ref: State[S, Int])(fa: Counter[A]): A = fa match {
  //   case Increment(a) => ???
  //   case Read(k) => ???
  //   case Reset(a) => a
  // }

  type CounterT[M[_], A] = FreeT[CounterF, M, A]

  // def incrementT[M[_]]: CounterT[M, Unit] =
  //   FreeT.liftF(Increment(()))
}

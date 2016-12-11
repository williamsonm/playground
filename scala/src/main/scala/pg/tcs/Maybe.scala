package tcs

sealed trait Maybe[A] extends Product with Serializable
final case class Just[A](a: A) extends Maybe[A]
final case class Nothin[A]() extends Maybe[A]

object Maybe {
  implicit val functorMaybe = new Functor[Maybe] with Applicative[Maybe] with Monad[Maybe] {
    def map[A,B](fa: Maybe[A])(f: A => B): Maybe[B] = fa match {
      case Just(a)   => Just(f(a))
      case Nothin() => Nothin[B]
    }

    def pure[A](a: A): Maybe[A] = Just(a)

    def ap[A, B](fa: Maybe[A])(f: Maybe[A => B]): Maybe[B] = (fa,f) match {
      case (Just(a), Just(f)) => Just (f(a))
      case _ => Nothin[B]
    }

    def flatMap[A,B](fa: Maybe[A])(f: A => Maybe[B]): Maybe[B] = fa match {
      case Just(a)   => f(a)
      case Nothin() => Nothin[B]()
    }
  }
  def nothin[A](): Nothin[A] = Nothin[A]
}

object MaybeUsage {
  import Maybe._
  import Functor.ops._

  val x: Maybe[Int] = Just(3)

  val result: Maybe[Int] = x as 4 // Just(4)

  val rstr: Maybe[String] = x as "wut" // Just("wut")

  def f(x: Int): String = s"$x + 3"

  x.map(f) == Just("3 + 3")

  def catMaybes[A](xs: List[Maybe[A]]): List[A] = xs match {
    case Just(h) :: t => h :: catMaybes(t)
    case _ :: t => catMaybes(t)
    case _ => List.empty
  }
}

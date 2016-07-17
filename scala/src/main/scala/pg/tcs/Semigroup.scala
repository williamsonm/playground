package tcs

import simulacrum._

@typeclass trait Semigroup[A] {
  @op("|+|") def append(x: A, y: A): A
}
@typeclass trait Monoid[A] extends Semigroup[A] {
  def id: A
}

object IntMonoids {
  implicit val Additive: Monoid[Int] = new Monoid[Int] {
    def id = 0
    def append(x: Int, y: Int) = x + y
  }
}

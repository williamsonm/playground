package pg

import shapeless._, poly._

object square extends Poly1 {
  implicit val is = at[Int] { i => i * i }
  implicit val fs = at[Float] { f => f * f }
  implicit val ds = at[Double] { d => d * d }
  implicit val ss = at[String] { s => s + s }
}

object test {

  val r = square(3)
  val i: Int = r

  // square("11") // does not compile

  val xs = 1 :: 2d :: 3f :: "4" :: HNil

  val result = xs map square // => 1 :: 4.0 :: 9.0 :: 44 :: HNil
}

package pg

import cats._
import cats.std.all._
import cats.syntax.all._

object eq {

  val x: Option[Int] = Some(3)
  val y: Option[Int] = Some(4)

  val r: Boolean = x === y // false
  val b: Boolean = x === x // true
}

class&(val x:Unit)extends AnyVal

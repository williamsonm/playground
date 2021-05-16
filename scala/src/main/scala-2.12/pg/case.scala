package pg

// @tpolecat: https://gist.github.com/tpolecat/a5cb0dc9adeacc93f846835ed21c92d2
// use abstract case class to prevent .copy() method from creating invalid instances

sealed abstract case class Nat(toInt: Int)

object Nat {
  def fromInt(x: Int): Option[Nat] =
    if (x>0) Some(new Nat(x) {}) else None
}

object usage {

  val x: Int = 3

  val n: Nat = Nat.fromInt(x).get
  val z: Int = n.toInt

  Nat.fromInt(3) == Nat.fromInt(3)

  // no .copy() method due to abstract case class
  // val nonNat: Nat = n.copy(toInt = -111)
}

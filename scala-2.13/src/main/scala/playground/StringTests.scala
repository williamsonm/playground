package playground

object StringTests {

  def genP(n: Int, k: Int): Int =
    n.toString().repeat(k).mkString.toInt

  def genP3(n: Int, k: Int): Int =
    (n.toString() * k).toInt

  def genP2(n: Int, k: Int): String = {
    val result =
      for {
        _ <- 1 to k
        s = n.toString
      } yield s

    result.mkString
  }

  def stringTest(s: String): String =
    s.repeat(3)
}

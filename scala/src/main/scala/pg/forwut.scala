package pg

object forwut {

  // strange for syntax that always returns Unit
  def run(): Unit = {
    val z = for {
      a <- Some("a")
      b <- Some("b")
    } {
      println(a ++ b)
    }
    println("z: " + z)
  }
}

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

  final case class Wut(classes: List[String])
  def error1 = ???
  def error2 = ???
  def error3 = ???
  def checkName(s: String): Boolean = true
  def checkVars(s: String): Boolean = true
  def checkMethods(s: String): Boolean = true

  def addToContext(s: String) = ???

  def run(program: Wut): Unit = {
    program.classes foreach {
      case klass if checkName(klass) => error1
      case klass if checkVars(klass) => error2
      case klass if checkMethods(klass) => error3
      case klass => addToContext(klass)
    }
  }
}

object lolz {
  import scalaz._, Scalaz._

  def length2(x:String) = List(x.length * 2)

  val x = "asdf" |> length2 |> (_.length)

  val y =
    "asdf" |>
    length2 |>
    (_.length)

  val z = List(123)
    .map(_+1)

  val zz = "asdf"
  .|> (length2)
  .|> (_.length)

  // val z = "asdf"
  //   .|> length2
    // .|> (_.length)
}

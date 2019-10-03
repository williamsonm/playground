import cats.implicits._

object errors {

    // no need for Validation?

    val e: Either[String, (Int, Int)] =
        ("error one".asLeft[Int], "error two".asLeft[Int]).parTupled

    // e === Left("error oneerror two")

    val e2: Either[String, (Int, Int)] =
        ("error one".asLeft[Int], "error two".asLeft[Int]).tupled

    // e2 === Left("error one")
}

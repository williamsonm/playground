import cats.implicits._
import cats.data.NonEmptyList

object errors {

    // no need for Validation?

    val e: Either[String, (Int, Int)] =
        ("error one".asLeft[Int], "error two".asLeft[Int]).parTupled

    // e === Left("error oneerror two")

    val e2: Either[String, (Int, Int)] =
        ("error one".asLeft[Int], "error two".asLeft[Int]).tupled

    // e2 === Left("error one")

    val e3: Either[NonEmptyList[String], (Int, Int)] =
        (NonEmptyList.one("error one").asLeft[Int], NonEmptyList.one("error two").asLeft[Int]).parTupled

    // e3 === Left("error one" :: "error two" :: Nil)
}

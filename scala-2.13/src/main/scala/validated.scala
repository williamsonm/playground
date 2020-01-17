import cats.implicits._
import cats.data.Validated
import cats.data.NonEmptyList

object validated {

  def parse(s: String): Validated[NonEmptyList[String], Int] =
    Validated
      .catchOnly[NumberFormatException](Integer.parseInt(s))
      .leftMap(_ => NonEmptyList.of(s"Not a valid integer: $s"))

  val one   = parse("1")
  val two   = parse("two")
  val three = parse("3.0")

  val result =
    (one, two, three).mapN((a, b, c) => a + b + c)

  // result === Invalid(NonEmptyList(Not a valid integer: two, Not a valid integer: 3.0))
}

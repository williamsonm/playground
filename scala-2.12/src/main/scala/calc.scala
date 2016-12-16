import cats._, cats.implicits._, cats.data._

trait Calc[F[_]] {
  def lit(i: Int): F[Int]

  def add(l: F[Int], r: F[Int]): F[Int]

  def sqrt(i: F[Int]): F[Double]
}

object CalcProgram extends App {
  def program[F[_]](calc: Calc[F]): F[Double] = {
    import calc._

    sqrt(add(lit(1), lit(2)))
  }

  object IdCalc extends Calc[Id] {
    def lit(i: Int): Id[Int] = i

    def add(l: Id[Int], r: Id[Int]): Id[Int] = (l + r)

    def sqrt(i: Id[Int]): Id[Double] = math.sqrt(i.toDouble)
  }

  object Serializer extends Calc[Const[String, ?]] {
    def lit(i: Int): Const[String, Int] = Const(i.toString)

    def add(l: Const[String, Int], r: Const[String, Int]): Const[String, Int] =
      Const(s"(${l.getConst} + ${r.getConst})")

    def sqrt(i: Const[String, Int]): Const[String, Double] =
      Const(s"sqrt(${i.getConst})")
  }

  val result: Double = program(IdCalc)
  val serialized: String = program(Serializer).getConst

  println(result)
  println(serialized)
}

import cats.data._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.traverse._

object kleisli {
  def k(a: String): Kleisli[Option, Int, Int] =
    Kleisli[Option, Int, Int](m =>
      Some(a.length * m)
    )

  val result: Kleisli[Option, Int, List[Int]] =
    List("hi", "hello").traverse(k)

  val r2: Option[List[Int]] = result.run(3)
}

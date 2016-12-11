import cats._, cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// scala.bythebay.io: Adelbert Chang, Spoiled by higher-kinded types
// https://www.youtube.com/watch?v=t7bOKAIQG4Q&app=desktop

object Traverses {
  def traverseFuture[A, B](as: List[A])(f: A => Future[B]): Future[List[B]] = {
    Future.sequence(as map f)
  }

  def traverse[F[_] : Applicative, A, B](as: List[A])(f: A => F[B]): F[List[B]] =
    as.foldRight(List.empty[B].pure[F]) { (a: A, acc: F[List[B]]) =>
      val fb = f(a)
      val t: F[(B, List[B])] = fb.product(acc)
      t.map { case (b, bs) => b :: bs }
    }

  val l = List(1,2,3)

  traverse(l)(i => Future { i * 2}): Future[List[Int]]
  traverse(l)(i => if (i % 2 == 0) Some(i/2) else None): Option[List[Int]]
  traverse[Either[String, ?], Int, Double](l)(i => if (i == 0) Left("oops") else Right(1.0 / i)): Either[String, List[Double]]
}

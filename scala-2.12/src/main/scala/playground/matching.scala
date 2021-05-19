package playground

import com.twitter.util.Future

object matching {

  def safe(): Future[Int] = {
    val foo: Future[Option[Int]] = Future.value(Some(3))
    for {
      Some(n) <- foo
    } yield n
  }

  def unsafe(): Future[Int] = {
    val foo: Future[Option[Int]] = Future.value(None)
    for {
      Some(n) <- foo
    } yield n
  }
}
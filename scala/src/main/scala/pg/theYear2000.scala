package pg

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

object theYear2000 extends App:

  val a = Future { Thread.sleep(1000); 2 }
  val b = Future { Thread.sleep(1000); 4 }
  val z = for
    x <- a
    y <- b
  yield x * y

  for wut <- z do println(s"Result: $wut")

  println("really")
  Thread.sleep(5000)
  println("done")

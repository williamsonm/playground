import scalaz.stream._
import scala.concurrent.duration._
import scalaz.concurrent.Task
import scalaz._, Scalaz._
import java.util.concurrent.atomic._
import java.util.concurrent.{ExecutorService,Executors}

import journal._

object Stream extends App {

  implicit val sc = new java.util.concurrent.ScheduledThreadPoolExecutor(1)

  val cancel = new java.util.concurrent.atomic.AtomicBoolean(false)

  val p = time.awakeEvery(1.second)
  p.map(println).run.runAsyncInterruptibly(_ => (), cancel)

  Thread.sleep(4000)
  println("Killing")
  cancel.set(true)
  println("Killed")

}

object Stream2 extends App {

  implicit val sc = new java.util.concurrent.ScheduledThreadPoolExecutor(1)

  val p = time.awakeEvery(1.second)
  val done = p.map(println(_)).run.runAsyncInterruptibly(_ => ())

  Thread.sleep(4000)
  println("Killing")
  done()
  println("Killed")
}


object Junk extends App {

    val logger = Logger[this.type]

    val neverMind = new AtomicBoolean(false)
    logger.info(s"neverMind set to $neverMind on thread ${Thread.currentThread().getName}")

    val t = Task.delay(System.out.println(s" in task run block on thread ${Thread.currentThread().getName}"))
      .map(_ => Thread.sleep(4000))
      .map(_ => System.out.println(s" completed sleep of 40000 ms on thread ${Thread.currentThread().getName}"))

    val t2 = Task {
      logger.info(s" in task run block on thread ${Thread.currentThread().getName}")
      Thread.sleep(4000)
      logger.info(s" completed sleep of 40000 ms on thread ${Thread.currentThread().getName}")
    }

    Task.fork(t).
    // t.
      runAsyncInterruptibly({
      case e => logger.error("wut")
    }, neverMind)


    logger.info("sleeping 1000, then set nevermind to true")
    Thread.sleep(1000)
    neverMind.set(true)
    logger.info("woke up. set cancel=true -- expect stack trace not 'Completed' message")

    Thread.sleep(4000)
}

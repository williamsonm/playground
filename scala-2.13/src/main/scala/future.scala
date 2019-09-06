import scala.concurrent._
import duration._, ExecutionContext.Implicits.global
import java.util.concurrent.CountDownLatch

object FutureMain extends App {

    val f = Future {
        val lock = new CountDownLatch(1)
        Future {
            Future { lock.countDown() }
            lock.await()
        }
    }.flatten

    Await.result(f, 5.seconds) // times out on 2.13
}

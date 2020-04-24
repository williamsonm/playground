import cats.effect._
import cats.implicits._
import skunk.Session
import natchez.Trace.Implicits.noop
import org.slf4j.LoggerFactory
import fs2.Pipe
import cats.effect.concurrent.Deferred
import cats.Show

object Main extends IOApp {

  val logger = LoggerFactory.getLogger(getClass)

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      user = "jimmy",
      database = "world",
      password = Some("banana")
    )

  def logData[T](implicit show: Show[T]): Pipe[IO, T, Unit] =
    _.map(data => logger.info(show.show(data)))

  def run(args: List[String]): IO[ExitCode] = {
    val program = fs2.Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
      sys.ShutdownHookThread {
        logger.info("shutting down")
        switch.complete(()).unsafeRunAsyncAndForget()
      }
      Graphics.dataStream
        .merge(Sensors.dataStream)
        .observe(logData)
        .through(TemperatureData.writeToDb(session))
        .interruptWhen(switch.get.attempt)
    }
    program.compile.drain.as(ExitCode.Success)
  }
}

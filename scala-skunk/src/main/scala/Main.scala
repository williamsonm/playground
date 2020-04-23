import cats.effect._
import cats.implicits._
import skunk.Session
import natchez.Trace.Implicits.noop
import org.slf4j.LoggerFactory
import fs2.Pipe
import cats.effect.concurrent.Deferred

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

  def logSensorData: Pipe[IO, Sensor, Unit] =
    _.map(sensor => logger.info(s"${sensor.name} => ${sensor.temp}"))

  def run(args: List[String]): IO[ExitCode] = {
    val program = fs2.Stream.eval(Deferred[IO, Unit]).flatMap { switch =>
      sys.ShutdownHookThread {
        logger.info("shutting down")
        switch.complete(()).unsafeRunAsyncAndForget()
      }
      Sensors.sensorStream
        .observe(logSensorData)
        .interruptWhen(switch.get.attempt)
    }
    program.compile.drain.as(ExitCode.Success)
  }
}

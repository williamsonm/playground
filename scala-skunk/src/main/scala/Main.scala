import cats.effect._
import io.circe.parser._
import skunk.Session
import natchez.Trace.Implicits.noop

object Hello extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      user = "jimmy",
      database = "world",
      password = Some("banana")
    )

  def run(args: List[String]): IO[ExitCode] =
    for {
      sensors <- Sensors.getSensorData().map(decode[Interfaces])
      _ <- IO(println(s"sensors: ${sensors}"))
    } yield ExitCode.Success
}

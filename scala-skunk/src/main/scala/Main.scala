import cats.implicits._
import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import natchez.Trace.Implicits.noop // (1)
import java.time.LocalDateTime
import java.time.OffsetDateTime

object Hello extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single( // (2)
      host = "localhost",
      port = 5432,
      user = "jimmy",
      database = "world",
      password = Some("banana")
    )

  val c: Command[String] =
    sql"DELETE FROM country WHERE name = $varchar".command

  val insertTemperature =
    sql"INSERT INTO temperature (location, value, updated) values ($varchar, $int4, $timestamptz)".command

  def times = {
    val n = LocalDateTime.now()
    n.plusMinutes(15)
  }

  def sampleData(s: Session[IO]) =
    s.prepare(insertTemperature).use(pc => pc.execute("downstairs" ~ 72 ~ OffsetDateTime.now()))

  val r = scala.util.Random

  def sampleTemperature(): Int =
    75 - r.nextInt(10)

  val tq =
    sql"SELECT location, value, updated FROM temperature".query(varchar ~ int4 ~ timestamptz)

  def tsz: List[OffsetDateTime] =
    (2500 to 2750).toList.map(n => OffsetDateTime.now().plusMinutes(n.toLong))

  def getSensors(): IO[String] = IO.delay {
    import scala.sys.process._
    "sensors -j".!!
  }

  def run(args: List[String]): IO[ExitCode] =
    session.use { s => // (3)
      for {
        z <- getSensors()
        _ <- IO(println(s"json: $z"))
        // c <- s.prepare(insertTemperature).use { pc =>
        //   tsz.traverse(n => pc.execute("downstairs" ~ sampleTemperature() ~ n))
        // }
        // _ <- IO(println(s"pc: $c"))
        // r <- s.execute(tq)
        // _ <- r.traverse_(row => IO(println(s"row: ${row}")))
      } yield ExitCode.Success
    }

}

/*
INSERT INTO some_table
 (ts_column)
 VALUES
 (TIMESTAMP '2011-05-16 15:36:38');
 */

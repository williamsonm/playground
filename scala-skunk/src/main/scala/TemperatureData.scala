import skunk.implicits._
import skunk.codec.all._
import java.time.LocalDateTime
import java.time.OffsetDateTime
import skunk.Session
import cats.effect.IO
import cats.effect.Resource
import cats.implicits._
import cats.effect.ExitCode
import fs2.Pipe
import skunk.data.Completion

object TemperatureData {
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

  def generateTemperatureData(session: Resource[IO, Session[IO]]): IO[ExitCode] =
    session.use { s =>
      for {
        c <- s.prepare(insertTemperature).use { pc =>
          tsz.traverse(n => pc.execute("downstairs" ~ sampleTemperature() ~ n))
        }
        _ <- IO(println(s"pc: $c"))
        r <- s.execute(tq)
        _ <- r.traverse_(row => IO(println(s"row: ${row}")))
      } yield ExitCode.Success
    }

  def writeToDb(session: Resource[IO, Session[IO]]): Pipe[IO, Sensor, Completion] =
    _.evalMap { sensor =>
      session.use { s =>
        s.prepare(insertTemperature).use { pc =>
          pc.execute(sensor.name ~ sensor.temp.input.toInt ~ OffsetDateTime.now())
        }
      }
    }
}

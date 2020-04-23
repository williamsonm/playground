import skunk.implicits._
import skunk.codec.all._
import java.time.LocalDateTime
import java.time.OffsetDateTime
import skunk.Session
import cats.effect.IO
import cats.effect.Resource
import cats.implicits._
import cats.effect.ExitCode

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
        c <- s.prepare(TemperatureData.insertTemperature).use { pc =>
          TemperatureData.tsz
            .traverse(n => pc.execute("downstairs" ~ TemperatureData.sampleTemperature() ~ n))
        }
        _ <- IO(println(s"pc: $c"))
        r <- s.execute(TemperatureData.tq)
        _ <- r.traverse_(row => IO(println(s"row: ${row}")))
      } yield ExitCode.Success
    }
}

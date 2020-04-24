import cats.effect.IO
import cats.implicits._
import scala.sys.process._
import io.circe.parser._
import fs2.Stream._
import fs2.{Pipe, Stream}
import scala.concurrent.duration._
import cats.effect.Timer

object Sensors {

  def getSensorData() =
    IO.delay("sensors -j".!!).map(decode[Interfaces])

  def toSensor: Pipe[IO, Either[io.circe.Error, Interfaces], Sensor] =
    _.mapFilter(_.map(_.interfaces).toOption)
      .flatMap(ni => emits(ni))
      .flatMap(i => emits(i.interface.sensors))

  def dataStream(implicit timer: Timer[IO]): Stream[IO, Sensor] =
    Stream
      .repeatEval(getSensorData)
      .metered(5.seconds)
      .through(toSensor)
}

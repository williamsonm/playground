import cats.effect.IO
import cats.implicits._
import scala.sys.process._
import scala.concurrent.duration._
import cats.effect.Timer
import fs2.Pipe
import fs2.Stream

object Graphics {

  def getGPUTemp() =
    IO.delay("nvidia-smi --query-gpu=temperature.gpu --format=csv,noheader".!!)
      .map(_.trim.toIntOption)

  def toSensor: Pipe[IO, Option[Int], Sensor] =
    _.mapFilter(
      _.map(temp => Sensor(name = "GeForce GTX 980 Ti", temp = Temperature(temp.toFloat)))
    )

  def dataStream(implicit timer: Timer[IO]): Stream[IO, Sensor] =
    Stream
      .repeatEval(getGPUTemp)
      .metered(5.seconds)
      .through(toSensor)
}

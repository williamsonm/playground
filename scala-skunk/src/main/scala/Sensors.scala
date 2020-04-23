import cats.effect.IO
import scala.sys.process._

object Sensors {

  def getSensorData(): IO[String] = IO.delay {
    "sensors -j".!!
  }
}

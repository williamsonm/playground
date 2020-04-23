import cats.effect.IO
import scala.sys.process._
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import io.circe.parser._
import org.slf4j.LoggerFactory
import fs2.Stream._
import fs2.Pipe
import scala.concurrent.duration._

object Sensors {

  implicit val timer = IO.timer(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

  private val logger = LoggerFactory.getLogger(getClass)

  def getSensorData() =
    IO.delay("sensors -j".!!).map(decode[Interfaces])

  val rawSensorData =
    fs2.Stream
      .repeatEval(getSensorData)
      .metered(3.second)

  def emitSensors: Pipe[IO, Either[io.circe.Error,Interfaces], Sensor] =
    _.flatMap(
      _.fold(
        error => {
          logger.error(s"Error processing sensor data: ${error.getMessage()}", error)
          empty
        },
        interfaces => emits(interfaces.interfaces)
      )
    ).flatMap(ni => emits(ni.interface.sensors))

  val sensorStream = rawSensorData.through(emitSensors)
}

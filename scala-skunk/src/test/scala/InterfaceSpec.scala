import org.specs2.Specification
import io.circe.parser._
import scala.io.Source
import org.slf4j.LoggerFactory

class InterfaceSpec extends Specification { def is = s2"""

  This is a specification for the 'Interface' json decoding

  The 'Interface' json should
    decode temperature                                $temperatureTest
    decode sensors                                    $sensorsTest
    decode interface                                  $interfaceTest
  """

  import Interfaces._
  import InterfaceJson._

  val logger = LoggerFactory.getLogger(getClass())

  val temperature = decode[Temperature](temperatureJson)
  def temperatureTest = temperature.isRight must_=== true

  val sensors = decode[List[Sensor]](sensorJson)
  def sensorsTest = sensors.isRight must_=== true

  val interfaceDecode = decode[Interface](sensorJson)
  def interfaceTest = interfaceDecode.isRight must_=== true

  val everything = decode[Interfaces](sampleJson)  

  everything.foreach { is =>
    is.interfaces.foreach { i =>
      logger.info(s"${i.name}: ${i.interface.sensors.length}")
    }
  }
}

object InterfaceJson {
  val temperatureJson = """{
        "temp1_input": 27.800,
        "temp1_crit": 105.000
     }"""

  val sensorJson = """{
     "Adapter": "ACPI interface",
     "temp1":{
        "temp1_input": 27.800,
        "temp1_crit": 105.000
     },
     "temp2":{
        "temp2_input": 29.800,
        "temp2_crit": 105.000
     }
  }"""

  val sampleJson = Source
    .fromFile("src/main/resources/sample.json")
    .mkString
}
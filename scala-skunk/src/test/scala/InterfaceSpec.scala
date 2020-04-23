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

  import Wut._

  def temperatureTest = temperature.isRight must_=== true
  def sensorsTest = sensors.isRight must_=== true
  def interfaceTest = interfaceDecode.isRight must_=== true
}

object Wut {

  val logger = LoggerFactory.getLogger(getClass)

  import Interface._

  val temperatureJson = """{
        "temp1_input": 27.800,
        "temp1_crit": 105.000
     }"""

  val temperature = decode[Temperature](temperatureJson)
  logger.info(s"temperature: $temperature")

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

  val sensors = decode[List[Sensor]](sensorJson)
  logger.info(s"sensors: $sensors")

  val interfaceDecode = decode[Interface](sensorJson)
  logger.info(s"interface: $interfaceDecode")

  val json = Source
    .fromFile("src/main/resources/sample.json")
    .mkString

  val everything = decode[Interfaces](json)
  logger.info(s"everything: $everything")
}
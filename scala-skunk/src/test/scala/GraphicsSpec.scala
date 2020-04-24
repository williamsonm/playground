import org.specs2.Specification
import org.slf4j.LoggerFactory

class GrahpicsSpec extends Specification { def is = s2"""

  This is a specification for the 'Graphics' json decoding

  The 'Graphics' json should
    decode temperature                                $temperatureTest
  """

  val logger = LoggerFactory.getLogger(getClass)

  val gpuTemp = Graphics.getGPUTemp().unsafeRunSync()

  logger.info(s"######### $gpuTemp")

  def temperatureTest = gpuTemp.isDefined
}
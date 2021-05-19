package playground

import org.scalatest._
import flatspec._
import matchers._
import org.slf4j.LoggerFactory

class MatchingSpec extends AnyFlatSpec with should.Matchers {

  val logger = LoggerFactory.getLogger(getClass)

  "matching" should "not throw a match error" in {
    matching.safe().foreach { n =>
      logger.error(s"got $n")
      n should be (3)
    }
  }
  it should "unsafe" in {
    val result = matching.unsafe()
    result.isDefined should be (true)
    result.foreach { n =>
      logger.error(s"got $n")
      n should be (None)
    }
  }
}
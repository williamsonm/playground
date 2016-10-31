package pg

import org.scalatest._
import org.scalatest.prop._

class BigDecimalSpec extends FlatSpec with PropertyChecks with Matchers {

  "big decimal" should "what" in {
    forAll { d: BigDecimal =>
      whenever (!(d.abs > 1 && d.toString.contains("E-"))) {

      }
    }
  }
}

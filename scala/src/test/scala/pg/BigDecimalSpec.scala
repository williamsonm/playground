package pg

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class BigDecimalSpec extends AnyFlatSpec with ScalaCheckDrivenPropertyChecks with should.Matchers {

  "big decimal" should "what" in {
    forAll { (d: BigDecimal) =>
      whenever (!(d.abs > 1 && d.toString.contains("E-"))) {
        println(s"$d")
      }
    }
  }
}

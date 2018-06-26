package core

import arrow.data.Reader
import arrow.data.map
import arrow.data.reader
import arrow.data.runId
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ReaderTest : StringSpec({
    "read" {
        val r: Reader<Int, Int> = { a: Int -> a * 2 }.reader()
        val m: Reader<Int, Int> = r.map { it -> it * 3 }
        val z: Int = m.runId(2)
        
        z shouldBe 12
    }
})

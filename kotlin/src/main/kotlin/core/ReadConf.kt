package core

import arrow.data.*

object ReadConf {

    fun test() {
        val r = { a: Int -> a * 2 }.reader()
        val m = r.map { it -> it * 3 }
        val z = m.runId(2)

        assert(z == 12)
    }

}

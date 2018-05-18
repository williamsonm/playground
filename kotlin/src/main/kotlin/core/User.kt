package core

import kotlin.io.*

data class LoggedInUser2(val userId: String, val name: String)

typealias F = (Int) -> Int

object User {

    fun <T> T.toNullable():T? = this
    fun <T, U> T?.flatMap(body: (T) -> U?): U? =
        if (this != null) body(this) else null

    fun f(x: Int): Int? = (x * 2)

    fun g(x: Int): String? = ("$x pounds")

    fun fThenG(x: Int): String? = f(x).flatMap { y -> g(y) }

    fun z(x: Int): F = {
        y -> x + y
    }
    fun x(f: F): Int = f.invoke(3)

    fun <A, B> F.andThen(g: F): F = {
        a -> g(this.invoke(a))
    }

    fun lolz(): F = {
        y -> y + 1
    }

    fun assertLaw1() {

        val value: Int = 2
        val lhs: Int? = f(value)
        val rhs: Int? = value.toNullable().flatMap { y -> f(y) }
        println("Satisfies law 1: ${lhs == rhs} ($lhs)")

        val lolz: Int? = 2
        println("Satisfies law 1: ${lolz == value} ($lolz)")
    }

    fun assertLaw2() {
        val monadValue = "Test".toNullable()
        val rhs = monadValue.flatMap { it.toNullable() }
        println("Satisfies law 2: ${monadValue == rhs} ($monadValue)")
    }

    fun assertLaw3() {
        val monadValue = 23.toNullable()
        val lhs = monadValue.flatMap { a -> f(a) }.flatMap { b -> g(b) }
        val rhs = monadValue.flatMap { a -> fThenG(a) }
        println("Satisfies law 3: ${lhs == rhs} (${lhs})")
    }

}

fun main(args : Array<String>) {
  User.assertLaw1()
  User.assertLaw2()
  User.assertLaw3()
}

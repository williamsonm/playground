package pg
package eff

import cats.data._
import org.atnos.eff._, all._
import org.atnos.eff.syntax.all._

object first {
  // type Stack = Reader[Int, ?] |: Writer[String, ?] |: Eval |: NoEffect
  //
  // val program: Eff[Stack, Int] = for {
  //   n <- ask
  //   _ <- tell("the required power is "+n)
  //   a <- delay(math.pow(2, n.toDouble).toInt)
  //   _ <- tell("the result is "+a)
  // } yield a
  //
  // program.runReader(6).runWriter.runEval.run

  delay(1 + 1).runEval.run

  type S = Option |: NoEffect

  val map: Map[String, Int] =
    Map("key1" -> 10, "key2" -> 20)

  def addKeys(key1: String, key2: String): Eff[S, Int] = for {
    a <- fromOption(map.get(key1))
    b <- fromOption(map.get(key2))
  } yield a + b
}

object second {
  type S = Option |: Eval |: NoEffect

  def delayed(x: Int) = delay[S, Int](x+1)

  def optioned(x: Int) = OptionEffect.some[S, Int](x)

  def add(x: Int): Eff[S, Int] = for {
    a <- delayed(x)
    b <- optioned(x)
  } yield a + b

  val result: Option[Int] = add(3).runEval.runOption.run
}

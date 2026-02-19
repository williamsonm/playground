package playground

import collection.mutable.Stack
import org.scalatest._
import flatspec._
import matchers._
import StringTests._

class ExampleSpec extends AnyFlatSpec with should.Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }

  it should "genP3" in {
    genP3(123, 3) === 123123123
  }

  it should "genP2" in {
    val result = genP2(123, 3)
    result should be ("123123123")
  }
}
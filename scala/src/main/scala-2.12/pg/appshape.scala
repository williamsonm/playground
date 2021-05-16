package pg

import scalaz._
import Scalaz._

case class Foo(a: Int, b: Char, c: String)

object sz {
  type ErrorsOr[A] = ValidationNel[String, A]
  type Validator[A] = String => ErrorsOr[A]

  val checkA: Validator[Int] = (s: String) =>
     try s.toInt.success catch {
     case _: NumberFormatException => "Not a number!".failureNel
  }

  val checkB: Validator[Char] = (s: String) =>
    if (s.size != 1 || s.head < 'a' || s.head > 'z') {
      "Not a lower case letter!".failureNel
    } else s.head.success

  val checkC: Validator[String] = (s: String) =>
    if (s.size == 4) s.success else "Wrong size!".failureNel


  def validateFoo(a: String, b: String, c: String) =
      (checkA(a) |@| checkB(b) |@| checkC(c))(Foo.apply _)

  println(validateFoo("ab", "cd", "ef"))
  //Failure(NonEmpty[Not a number!,Not a lower case letter!,Wrong size!])
  println(validateFoo("42", "cd", "ef"))
  //Failure(NonEmpty[Not a lower case letter!,Wrong size!])

  def validateFoo2(a: String, b: String, c: String):Validation[NonEmptyList[String], Foo] =
    checkC(c) <*> (checkB(b) <*> (checkA(a) map (Foo.apply _).curried))

  println(validateFoo2("42", "cd", "ef"))
  //Failure(NonEmpty[Not a lower case letter!,Wrong size!])

  import shapeless._
  import shapeless.ops.hlist._
  import shapeless.ops.function._
  import shapeless.poly._
  import shapeless.syntax.std.function._

  object applier extends Poly2 {
    implicit def ap[F[_]: Applicative, H, T <: HList, R]:
      Case2.Aux[applier.type, F[(H :: T) => R], F[H], F[T => R]] =
      at[F[(H :: T) => R], F[H]](
        (f, fa) => fa <*> f.map(hf => (h: H) => (t: T) => hf(h :: t))
      )
  }

  class Lifter[F[_]: Applicative] {
    def lift[G, H, A <: HList, M <: HList, R](g: G)(implicit
      hlG: FnToProduct.Aux[G, A => R],
      mapped: Mapped.Aux[A, F, M],
      unH: FnFromProduct.Aux[M => F[R], H],
      folder: LeftFolder.Aux[M, F[A => R], applier.type, F[HNil => R]]
    ) = unH((m: M) => folder(m, hlG(g).point[F]).map(_(HNil)))
  }

  def into[F[_]: Applicative] = new Lifter[F]

  val liftedFoo = into[ErrorsOr] lift (Foo.apply _)

  def validateFooGeneric(a: String, b: String, c: String) =
    liftedFoo(checkA(a), checkB(b), checkC(c))

  println(validateFooGeneric("42", "cd", "ef"))
  //Failure(NonEmpty[Not a lower case letter!,Wrong size!])

  def validate[F[_], G, H, V <: HList, I <: HList, M <: HList, A <: HList, R]
    (g: G)(v: V)(implicit
    hlG: FnToProduct.Aux[G, A => R],
    zip: ZipApply.Aux[V, I, M],
    mapped: Mapped.Aux[A, F, M],
    unH: FnFromProduct.Aux[I => F[R], H],
    folder: LeftFolder.Aux[M, F[A => R], applier.type, F[HNil => R]],
    appl: Applicative[F]
    ) = unH((in: I) => folder(zip(v, in), hlG(g).point[F]).map(_(HNil)))

  val validateFooShapeless = validate(Foo.apply _)(checkA :: checkB :: checkC :: HNil)
}

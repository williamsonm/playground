package pg
package json

object transform extends App {

  import cats.free.Trampoline
  import cats.std.list._
  import cats.syntax.traverse._
  import io.circe.{ Json, JsonObject }

  def transformObjectKeys(obj: JsonObject, f: String => String): JsonObject =
    JsonObject.fromIterable(
      obj.toList.map {
        case (k,v) => f(k) -> v
      }
    )

  def transformKeys(json: Json, f: String => String): Trampoline[Json] =
    json.arrayOrObject(
      Trampoline.done(json),
      _.traverse(j => Trampoline.suspend(transformKeys(j, f))).map(Json.fromValues),
      transformObjectKeys(_, f).traverse(obj => Trampoline.suspend(transformKeys(obj, f))).map(Json.fromJsonObject)
    )

  def sc2cc(in: String) = "_([a-z\\d])".r.replaceAllIn(in, _.group(1).toUpperCase)

  import io.circe.literal._
  val doc = json"""
    {
      "first_name" : "foo",
      "last_name" : "bar",
      "parent" : {
        "first_name" : "baz",
        "last_name" : "bazz"
      }
    }
  """

  import cats.std.function._

  val result: Json = transformKeys(doc, sc2cc).run

  println(doc)
  println(result)
}

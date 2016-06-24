package pg
package json

import argonaut._, Argonaut._, Shapeless._

object auto {

  case class CC(i: Int, s: String)

  val encode = EncodeJson.of[CC]

  // sealed trait Body
  // case class BodyA(site: String) extends Body
  // case class BodyB(site: String) extends Body
  //
  // val encode = EncodeJson.of[Body]
  //
  // val json = encode(BodyA(3))
}

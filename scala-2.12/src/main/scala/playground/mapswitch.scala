package playground

object mapswitch {

  final case class UserId(id: Int) extends AnyVal
  final case class AddressId(id: Int) extends AnyVal

  val m: Map[UserId, List[AddressId]] = Map(
    UserId(1) -> List(AddressId(11), AddressId(12)),
    UserId(2) -> List(AddressId(11), AddressId(13))
  )

  val zresult: Map[AddressId, List[UserId]] = switch(m)

  def switch(m: Map[UserId, List[AddressId]]): Map[AddressId, List[UserId]] = {
    val empty = Map.empty[AddressId, List[UserId]]
    m.foldLeft(empty) { case (result, (user, addresses)) =>
      result ++ addresses.map(a =>
        (a, result.getOrElse(a, List.empty) :+ user)
      )
    }
  }
}

object mapswitch {

  final case class UserId(id: Int) extends AnyVal
  final case class AddressId(id: Int) extends AnyVal

  val m: Map[UserId, List[AddressId]] = Map(
    UserId(1) -> List(AddressId(11), AddressId(12)),
    UserId(2) -> List(AddressId(11), AddressId(13))
  )

  val result: Map[AddressId, List[UserId]] = ???

  def switch(m: Map[UserId, List[AddressId]]): Map[AddressId, List[UserId]] = {
    val empty = Map.empty[AddressId, List[UserId]]
    m.foldLeft(empty) { case (result, (user, addresses)) =>

      val as: List[UserId] = addresses.map(a => result.getOrElse(a, List.empty)).flatten

      result
    }
  }
}

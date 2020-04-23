import cats.implicits._
import io.circe._
import org.slf4j.LoggerFactory

final case class Temperature(
  input: Float,
  crit: Float
)

final case class Sensor(
  name: String,
  temp: Temperature
)

final case class Interface(adapter: String, sensors: List[Sensor])

final case class NamedInterface(name: String, interface: Interface)

final case class Interfaces(xs: List[NamedInterface])

object Interface {

  val logger = LoggerFactory.getLogger(getClass)  

  implicit val decodeTemperature: Decoder[Temperature] = j =>
    j.keys.map(_.toList) match {
      case Some(keys) => 
        val result = for {
          input <- keys.find(key => key.startsWith("temp") && key.endsWith("_input"))
          crit <- keys.find(key => key.startsWith("temp") && key.endsWith(("_crit")))
          inputValue <- j.downField(input).as[Float].toOption
          critValue <- j.downField(crit).as[Float].toOption
        } yield Temperature(inputValue, critValue)

        result.toRight(DecodingFailure("missing temp keys", Nil))

      case None => Left(DecodingFailure("failed to parse temperature", Nil))
    }

  implicit val decodeSensor: Decoder[List[Sensor]] = j =>
    j.keys.map(_.toList) match {
      case Some(keys) =>
        val result = keys.map(key => j.downField(key).as[Temperature].map(temp => Sensor(name = key, temp = temp)))

        if (result.exists(_.isRight)) {

          val zs: Either[DecodingFailure, List[Sensor]] = result.filter(_.isRight).sequence
          val xs = zs.getOrElse(Nil)


          Right(xs)
        } else {
          Left(DecodingFailure("no temps exist", Nil))
        }


      case None => Left(DecodingFailure("failed to parse sensor", Nil))
    }

  implicit val decoder: Decoder[Interface] = j => j.keys.map(_.toList) match {
    case Some(keys) => {
      val result = for {
        adapterKey <- keys.find(_ === "Adapter")
        adapter <- j.downField(adapterKey).as[String].toOption
        rest <- j.as[List[Sensor]].toOption
      } yield Interface(adapter, rest)

      result.toRight(DecodingFailure.apply("failed parsing interface", Nil))
    }
    case None => Left(DecodingFailure.apply("what", Nil))
  }

  implicit val decodeInterfaces: Decoder[Interfaces] = o =>
    o.keys.map(_.toList) match {
      case Some(keys) =>       
        val result = keys.map(name => 
          for {
            i <- o.downField(name).as[Interface].map(interface => NamedInterface(name, interface))
            _ = logger.info(s"decoding name: $name")
          } yield i
        )
        result.filter(_.isRight).sequence.map(Interfaces(_))

      case None => Left(DecodingFailure("interfaces have no keys", Nil))
    }
}
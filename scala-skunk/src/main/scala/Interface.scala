import cats.implicits._
import io.circe._
import io.circe.generic.semiauto._
import cats.Show

final case class Temperature(
    input: Float
)

final case class Sensor(
    name: String,
    temp: Temperature
)

final case class Interface(adapter: String, sensors: List[Sensor])

final case class NamedInterface(name: String, interface: Interface)

final case class Interfaces(interfaces: List[NamedInterface])

object Sensor {
  implicit val show: Show[Sensor] = Show.fromToString
}

object Interfaces {
  implicit val decodeTemperature: Decoder[Temperature] = obj =>
    obj.keys.map(_.toList) match {
      case Some(keys) =>
        val result = for {
          input <- keys.find(key => key.startsWith("temp") && key.endsWith("_input"))
          inputValue <- obj.downField(input).as[Float].toOption
        } yield Temperature(inputValue)

        result.toRight(DecodingFailure("missing temp keys", Nil))

      case None => Left(DecodingFailure("failed to parse temperature", Nil))
    }

  implicit val decodeSensor: Decoder[List[Sensor]] = obj =>
    obj.keys.map(_.toList) match {
      case Some(keys) =>
        val result =
          keys.map(name => obj.downField(name).as[Temperature].map(temp => Sensor(name, temp)))
        if (result.exists(_.isRight)) {
          result.filter(_.isRight).sequence
        } else {
          result.sequence
        }
      case None => Left(DecodingFailure("failed to parse sensor", Nil))
    }

  implicit val decoder: Decoder[Interface] = obj =>
    obj.keys.map(_.toList) match {
      case Some(keys) => {
        val result = for {
          adapterKey <- keys.find(_ === "Adapter")
          adapter <- obj.downField(adapterKey).as[String].toOption
          sensors <- obj.as[List[Sensor]].toOption
        } yield Interface(adapter, sensors)

        result.toRight(DecodingFailure.apply("failed parsing interface", Nil))
      }
      case None => Left(DecodingFailure.apply("what", Nil))
    }

  implicit val decodeInterfaces: Decoder[Interfaces] = o =>
    o.keys.map(_.toList) match {
      case Some(keys) =>
        val result = keys.map(name =>
          o.downField(name).as[Interface].map(interface => NamedInterface(name, interface))
        )
        result.filter(_.isRight).sequence.map(Interfaces(_))

      case None => Left(DecodingFailure("interfaces have no keys", Nil))
    }

  implicit val encoder1: Encoder[Temperature] = deriveEncoder[Temperature]
  implicit val encoder2: Encoder[Sensor] = deriveEncoder[Sensor]
  implicit val encoder3: Encoder[Interface] = deriveEncoder[Interface]
  implicit val encoder4: Encoder[NamedInterface] = deriveEncoder[NamedInterface]
  implicit val encoder5: Encoder[Interfaces] = deriveEncoder[Interfaces]
}

package fs2pg

sealed trait ConversionResult
final case class ConversionSuccess(path: String) extends ConversionResult
final case class ConversionError(message: String, path: String, underlying: Throwable) extends ConversionResult

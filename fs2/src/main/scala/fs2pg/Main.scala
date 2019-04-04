package fs2pg

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.{Pipe, Stream}
import org.slf4j.LoggerFactory

object Main extends IOApp {

  type Path = String

  private val logger = LoggerFactory.getLogger(getClass)

  def run(args: List[String]): IO[ExitCode] =
    for {
      result <- multiStream.compile.foldMonoid
      _ <- IO(logger.info(s"result: $result"))
      exitCode <- IO(logger.info("done")).as(ExitCode.Success)
    } yield exitCode

  def multiStream: Stream[IO, Stats] =
    runQuery()
      .through(convert)
      .observe(save)
      .through(stats)

  def runQuery(): Stream[IO, Path] = {
    logger.info("run query")
    val paths = List("/content/ogj/en/one", "/content/ogj/en/two")
    Stream.emits(paths).repeatN(1000)
  }

  def convert: Pipe[IO, Path, ConversionResult] =
    _.map(s => {
      val pageName = s.substring(s.lastIndexOf("/") + 1)
      logger.trace(s"convert: $pageName")
      ConversionSuccess(pageName)
    })

  def saveIO(path: String): IO[Unit] =
    IO(logger.info(s"saving: $path"))

  def save: Pipe[IO, ConversionResult, Unit] =
    _.map {
      case ConversionSuccess(path) => logger.trace(s"saving: $path")
      case _ =>
    }

  def stats: Pipe[IO, ConversionResult, Stats] =
    _.map(s => {
      logger.trace(s"stats: $s")
      Stats(1, 0)
    })
}

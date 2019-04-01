package fs2pg

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.{Pipe, Stream}
import org.slf4j.LoggerFactory

final case class Result(value: String)

object Main extends IOApp {

  private val logger = LoggerFactory.getLogger(getClass)

  def run(args: List[String]): IO[ExitCode] =
    for {
      result <- multiStream.compile.toList
      _ <- IO(logger.info(s"result: $result"))
      exitCode <- IO(logger.info("done")).as(ExitCode.Success)
    } yield exitCode

  def multiStream: Stream[IO, Result] = {
    val input = runQuery().through(convert)

    input.flatMap { in =>
      val left = Stream(in).through(save)
      val right = Stream(in).through(stats)

      left >> right
    }
  }

  def runQuery(): Stream[IO, String] = {
    logger.info("run query")
    Stream("/content/ogj/en/one", "/content/ogj/en/two")
  }

  def convert: Pipe[IO, String, String] =
    _.map(s => {
      val pageName = s.substring(s.lastIndexOf("/") + 1)
      logger.info(s"convert: $pageName")
      pageName
    })

  def save: Pipe[IO, String, Unit] =
    _.map(s => logger.info(s"saving: $s"))

  def stats: Pipe[IO, String, Result] =
    _.map(s => {
      logger.info(s"stats: $s")
      Result(s)
    })
}

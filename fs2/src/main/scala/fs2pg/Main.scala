package fs2pg

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import fs2.{Pipe, Stream}
import org.slf4j.LoggerFactory

import scala.util.Random

object Main extends IOApp {

  type Path = String
  type SessionId = Int
  type Session = Resource[IO, SessionId]

  final val NUM_SESSIONS = 8

  private val logger = LoggerFactory.getLogger(getClass)

  def run(args: List[String]): IO[ExitCode] =
    mkPipes.use { pipes =>
      for {
        result <- multiStream(pipes).compile.foldMonoid
        _ <- IO(logger.info(s"result: $result"))
        exitCode <- IO(logger.info("done")).as(ExitCode.Success)
      } yield exitCode
    }

  def multiStream(pipes: List[Pipe[IO, Path, ConversionResult]]): Stream[IO, Stats] =
    runQuery()
      .balanceThrough(NUM_SESSIONS)(pipes:_*)
      .observe(save)
      .through(stats)

  def runQuery(): Stream[IO, Path] = {
    logger.info("run query")
    val paths = List("/content/ogj/en/one", "/content/ogj/en/two")
    Stream.emits(paths).repeatN(10)
  }

  def convert: Pipe[IO, Path, ConversionResult] =
    _.chunkN(100, allowFewer = true)
      .mapAsync(NUM_SESSIONS) { chunk =>
        connect(Random.nextInt).use { sessionId =>
          IO(chunk.map(path => ConversionSuccess(sessionId, path)))
        }
      }
      .flatMap(Stream.chunk)

  def connect(id: Int): Resource[IO, SessionId] = {
    val acquire = IO(id)
    def release(session: Int) = IO(logger.info(s"Session #$id closed."))
    Resource.make(acquire)(release)
  }

  def mkPipes: Resource[IO, List[Pipe[IO, Path, ConversionResult]]] =
    connect(Random.nextInt).replicateA(NUM_SESSIONS).map(_.map(mkPipe))

  def mkPipe(sessionId: SessionId): Pipe[IO, Path, ConversionResult] =
    _.map { path =>
      logger.info(s"pipe: $sessionId")
      ConversionSuccess(sessionId, path)
    }

  def save: Pipe[IO, ConversionResult, Unit] =
    _.map {
      case ConversionSuccess(id, path) => logger.trace(s"$id saving: $path")
      case _                       =>
    }

  def stats: Pipe[IO, ConversionResult, Stats] =
    _.map(s => {
      logger.trace(s"stats: $s")
      Stats(1, 0)
    })
}

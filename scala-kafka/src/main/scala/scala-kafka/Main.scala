import cats.syntax.all._
import cats.effect._
import org.slf4j.LoggerFactory;

object Main extends IOApp {

  val logger = LoggerFactory.getLogger(getClass())

  def run(args: List[String]): IO[ExitCode] =
    IO(logger.info(s"Hello world!. Args $args")) *> IO(ExitCode.Success)
}

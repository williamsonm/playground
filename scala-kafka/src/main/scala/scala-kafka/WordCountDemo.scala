import cats.effect._

import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.Properties

object WordCountDemo extends IOApp {

  val logger = LoggerFactory.getLogger(getClass)

  val INPUT_TOPIC = "streams-plaintext-input"
  val OUTPUT_TOPIC = "streams-wordcount-output"

  def getConfig() = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }

  def buildTopology() = {
    import Serdes._

    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] = builder.stream[String, String](INPUT_TOPIC)
    val wordCounts: KTable[String, Long] = textLines
      .flatMapValues(textLine => textLine.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()

    wordCounts.toStream.to(OUTPUT_TOPIC)
    builder.build()
  }

  def run(args: List[String]): IO[ExitCode] = {
    val streams = new KafkaStreams(buildTopology, getConfig)
    streams.start()

    sys.ShutdownHookThread {
      streams.close(10, TimeUnit.SECONDS)
    }
    IO(ExitCode.Success)
  }
}

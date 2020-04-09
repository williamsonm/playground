import cats.effect._

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import java.util.Properties;
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object WordCountDemo extends IOApp {

  val logger = LoggerFactory.getLogger(getClass)

  val INPUT_TOPIC = "streams-plaintext-input"
  val OUTPUT_TOPIC = "streams-wordcount-output"

  def getStreamsConfig(): Properties = {
    val props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props;
  }

  def run(args: List[String]): IO[ExitCode] = {
    import Serdes._

    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] = builder.stream[String, String](INPUT_TOPIC)
    val wordCounts: KTable[String, Long] = textLines
      .flatMapValues(textLine => textLine.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()

    wordCounts.toStream.to(OUTPUT_TOPIC)

    val streams: KafkaStreams = new KafkaStreams(builder.build(), getStreamsConfig)
    streams.start()

    sys.ShutdownHookThread {
      streams.close(10, TimeUnit.SECONDS)
    }
    IO(ExitCode.Success)
  }
}

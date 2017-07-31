package KafkaSalesForce

class KafkaConsumerAndProducer {

  import cakesolutions.kafka.KafkaConsumer.Conf
  import scala.collection.JavaConversions._
  import cakesolutions.kafka.{KafkaConsumer, KafkaProducer, KafkaProducerRecord}
  import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
  import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

  val consumer = KafkaConsumer(
    Conf(new StringDeserializer(), new StringDeserializer(),
      bootstrapServers = "localhost:9092",
      groupId = "group",
      enableAutoCommit = true,
      autoCommitInterval = 1000,
      sessionTimeoutMs = 30000,
      maxPollRecords = Integer.MAX_VALUE,
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )

  def consumerResults(topic: String): List[String] = {
    consumer.subscribe(Seq(topic))
    val records: ConsumerRecords[String, String] = consumer.poll(100)
    val results: List[String] = (for (record <- records) yield record.value()).toList
    results
  }

  val producer = KafkaProducer(
    KafkaProducer.Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092",
      acks = "all",
      retries = 0,
      batchSize = 16834,
      lingerMs = 1,
      bufferMemory = 33554432)
  )

  def producerUpdate(topic: String, key: String, value: String): Unit={
    producer.send(KafkaProducerRecord(topic, key, value))
  }

}

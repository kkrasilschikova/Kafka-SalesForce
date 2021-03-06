package KafkaSalesForce

class KafkaConsumerAndProducer(bootstrapServers: String) {

  import cakesolutions.kafka.KafkaConsumer.Conf
  import cakesolutions.kafka.{KafkaConsumer, KafkaProducer, KafkaProducerRecord}
  import org.apache.kafka.clients.consumer.{ConsumerRecords, OffsetResetStrategy}
  import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
  import play.api.libs.json.JsObject

  import scala.collection.JavaConversions._

  val consumer = KafkaConsumer(
    Conf(new StringDeserializer(), new StringDeserializer(),
      bootstrapServers = bootstrapServers,
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
    (for (record <- records) yield record.value()).toList
  }

  val producer = KafkaProducer(
    KafkaProducer.Conf(keySerializer=new StringSerializer(),
      valueSerializer=new JsonSerializer(),
      bootstrapServers = bootstrapServers,
      acks = "all",
      retries = 0,
      batchSize = 16834,
      lingerMs = 1,
      bufferMemory = 33554432)
  )

  def producerUpdate(topic: String, key: String, value: JsObject): Unit = {
    producer.send(KafkaProducerRecord(topic, key, value))
  }

}

object KafkaConsumerAndProducer {
  def apply(bootstrapServers: String): KafkaConsumerAndProducer = new KafkaConsumerAndProducer(bootstrapServers=bootstrapServers)
}


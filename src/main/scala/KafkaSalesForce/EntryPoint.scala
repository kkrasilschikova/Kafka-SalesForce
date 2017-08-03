package KafkaSalesForce

object EntryPoint {
  case class Config(kafkaIpPort: String="", sourceTopic: String="", targetTopic: String="")

  def main(args: Array[String]): Unit = {
    val parser=new scopt.OptionParser[Config]("KafkaSalesForce") {
      opt[String]("kafkaIP:port").required().valueName("<kafka-IP:9092").
        action((x,c) => c.copy(kafkaIpPort=x))
      opt[String]("sourceTopic").required().valueName("<source-kafka-topic>").
        action((x,c) => c.copy(sourceTopic=x))
      opt[String]("targetTopic").required().valueName("<target-kafka-topic>").
        action((x,c) => c.copy(targetTopic=x))
    }
    parser.parse(args, Config()) match {
      case Some(config) =>

        val kafka = new KafkaConsumerAndProducer(config.kafkaIpPort)
        val cases: List[String] = kafka.consumerResults(config.sourceTopic)

        val sf = new SalesForceConnectAndQuery
        sf.connect
        val sfResults: List[String] = sf.accountNameFromSF(cases)

        for (value <- sfResults) kafka.producerUpdate(config.targetTopic, "AccountLookup", value)
        //kafka.consumerResults(config.targetTopic)//just to check target topic
      case None => // arguments are bad, error message will be displayed
    }
  }

}

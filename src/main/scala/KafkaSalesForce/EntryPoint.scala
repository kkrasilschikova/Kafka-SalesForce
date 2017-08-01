package KafkaSalesForce

object EntryPoint {
  case class Config(sourceTopic: String="", targetTopic: String="")

  def main(args: Array[String]): Unit = {
    val parser=new scopt.OptionParser[Config]("KafkaSalesForce") {
      opt[String]('s', "sourceTopic").required().valueName("<source-kafka-topic>").
        action((x,c) => c.copy(sourceTopic=x))
      opt[String]('t', "targetTopic").required().valueName("<target-kafka-topic>").
        action((x,c) => c.copy(targetTopic=x))
    }
    parser.parse(args, Config()) match {
      case Some(config) =>

        //args(0)="SF_query_requests"
        //args(1)="SF_query_results"

        val kafka = new KafkaConsumerAndProducer
        val cases: List[String] = kafka.consumerResults(args(0))

        val sf = new SalesForceConnectAndQuery
        sf.connect
        val sfResults: List[String] = sf.accountNameFromSF(cases)

        for (value <- sfResults) kafka.producerUpdate(args(1), "key", value)

      case None => // arguments are bad, error message will have been displayed
    }
  }
}

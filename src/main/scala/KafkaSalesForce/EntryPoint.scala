package KafkaSalesForce

object EntryPoint {
  def main(args: Array[String]): Unit = {
    args(0)="SF_query_requests"
    args(1)="SF_query_results"

    val kafka=new KafkaConsumerAndProducer
    val cases: List[String]=kafka.consumerResults(args(0))

    val sf=new SalesForceConnectAndQuery
    sf.connect
    val sfResults: List[String]=sf.accountNameFromSF(cases)

    for (value<-sfResults) kafka.producerUpdate(args(1), "key", value)
  }
}

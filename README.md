# Kafka-SalesForce
#### Purpose:
Read source topic from Kafka (value is "caseNumber"), find corresponding account name in Salesforce and write to target topic to Kafka (key will be "AccountLookup", value will be "caseNumber;accountName").  
There should be SALESFORCE_USERNAME and SALESFORCE_PASSWORD environment variables.

#### Library dependencies:
```
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.11.0.0"
libraryDependencies += "com.force.api" % "force-partner-api" % "40.0.0"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.6.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
```

#### In order to run the program

- run `sbt assembly`
- run jar with parameters  
`java -jar Kafka-SalesForce-assembly-1.0.jar --kafkaIpPort localhost:9092 --sourceTopic sourceTopicName --targetTopic targetTopicName`

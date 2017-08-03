# Kafka-SalesForce
#### Purpose:
Read source topic from Kafka (value is "caseNumber"), find corresponding account name in Salesforce and write to target topic to Kafka (key will be "AccountLookup", value will be "caseNumber;accountName").  

#### Prerequisites:
SALESFORCE_USERNAME and SALESFORCE_PASSWORD environment variables.

#### In order to run the program

- run *sbt assembly*
- run jar with parameters *java -jar Kafka-SalesForce-assembly-1.0.jar --kafkaIpPort localhost:9092 --sourceTopic sourceTopicName --targetTopic targetTopicName*

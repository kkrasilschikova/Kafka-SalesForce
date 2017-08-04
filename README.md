# Kafka-SalesForce
#### Purpose:
Read source topic from Kafka (value is "caseNumber"), find corresponding account name in Salesforce and write to target topic to Kafka (key will be "AccountLookup", value will be {"caseNumber":"01234567","accountName":"Name"}).

#### Prerequisites:
SALESFORCE_USERNAME and SALESFORCE_PASSWORD environment variables. SALESFORCE_PASSWORD should have a format like passwordSecurityToken.

#### In order to run the program

- run *sbt assembly*
- run jar with parameters *java -jar Kafka-SalesForce-assembly-1.0.jar --kafkaIpPort localhost:9092 --sourceTopic sourceTopicName --targetTopic targetTopicName*

#### How to generate SalesForce security token

1) login to SalesForce
2) in the top right corner click on your name -> My settings
3) click Personal -> Reset My Security Token, or enter 'reset' in quick find
4) you'll receive an e-mail with a new token
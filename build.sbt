name := "Kafka-SalesForce"

version := "1.0"

scalaVersion := "2.12.3"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "0.11.0.0"

libraryDependencies += "com.force.api" % "force-partner-api" % "40.0.0"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25" % "test"

libraryDependencies += "com.github.scopt" % "scopt_2.11" % "3.6.0"
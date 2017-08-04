package KafkaSalesForce

import com.sforce.soap.partner.PartnerConnection
import com.sforce.ws.ConnectorConfig
import play.api.libs.json.{JsObject, Json}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

class SalesForceConnectAndQuery {
  def connect: Try[PartnerConnection] = {
    val servicesEndpointSuffix = "services/Soap/u/40.0/"
    val authEndPoint = "https://login.salesforce.com/" + servicesEndpointSuffix

    val configTry: Try[ConnectorConfig] = {
      val maybeUsername = sys.env.get("SALESFORCE_USERNAME")
      val maybePassword = sys.env.get("SALESFORCE_PASSWORD")
      val usernameTry = maybeUsername
        .fold[Try[String]](Failure(new Error("You must specify the SALESFORCE_USERNAME env var")))(Success(_))
      val passwordTry = maybePassword
        .fold[Try[String]](Failure[String](new Error("You must specify the SALESFORCE_PASSWORD env var")))(Success(_))

      for {
        username <- usernameTry
        password <- passwordTry
      } yield new ConnectorConfig() {
        setUsername(username)
        setPassword(password)
        setAuthEndpoint(authEndPoint)
      }
    }

    for {
      config <- configTry
      connection <- Try(new PartnerConnection(config))
      instanceUrl <- config.getServiceEndpoint
        .split(servicesEndpointSuffix)
        .headOption
        .fold[Try[String]](Failure(new Error("Could not parse the instance url")))(Success(_))
    } yield connection
  }

  def accountNameFromSF(l: List[String]): List[JsObject] = {
    @tailrec
    def getAccountAndCase(l: List[String], acc: List[java.io.Serializable]): List[java.io.Serializable] = {
      l match {
        case Nil => acc
        case head :: tail =>
          val xmlResult = connect.get
            .query(s"SELECT Account.Name, CaseNumber FROM Case WHERE CaseNumber='$head'")
          val caseAccount = for (record <- xmlResult.getRecords) yield
            (record.getField("CaseNumber"), record.getChild("Account").getField("Name"))
          getAccountAndCase(tail, acc ::: caseAccount.toList)
      }
    }

    def toListOfString(l: List[java.io.Serializable]): List[String] = {
      for (elem <- l) yield elem match {
        case (caseNumber, accountName) => s"$caseNumber;$accountName"
      }
    }

    def toListOfJson(l: List[java.io.Serializable]): List[JsObject]= {
      for (el <- l) yield el match {
        case (a, b) => Json.obj("caseNumber" -> s"$a", "accountName" -> s"$b")
      }
    }

    toListOfJson(getAccountAndCase(l, List()))
  }

}


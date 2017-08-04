package KafkaSalesForce

import java.util

import org.apache.kafka.common.serialization.Serializer
import play.api.libs.json.JsValue
class JsonSerializer extends Serializer[JsValue] {
  override def serialize(topic: String, data: JsValue): Array[Byte] =
    data.toString().getBytes("utf-8")

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}
}

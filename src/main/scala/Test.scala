package de.unruh.homeautomation

import org.eclipse.paho.mqttv5.client.{IMqttMessageListener, IMqttToken, MqttClient, MqttConnectionOptions}
import org.eclipse.paho.mqttv5.common.{MqttMessage, MqttSubscription}
import upickle.default.ReadWriter

import java.awt
import java.awt.Color
import scala.compiletime.uninitialized

class Mqtt {
  private val client = {
    val clientId = "de.unruh.homeautomation"
    val client = new MqttClient("tcp://quietscheentchen:1883", clientId)
    val options = new MqttConnectionOptions()
    options.setAutomaticReconnect(true)
    options.setConnectionTimeout(10)
    client.connect(options)
    client
  }

  subscribe("#", (topic, msg) => println(s"Received @ $topic: $msg"))

  def publish(topic: String, payload: String): Unit = {
    client.publish(topic, payload.getBytes, 0, false)
  }

  def subscribe(topic: String, callback: (String, String) => Unit): IMqttToken = {
    client.subscribe(
      Array(MqttSubscription(topic)),
      Array[IMqttMessageListener]((topic: String, message: MqttMessage) => {
        try {
          callback(topic, String(message.getPayload))
        } catch
          case e: Throwable =>
            e.printStackTrace()
      }))
  }
}

trait OnOff {
  def isOn: Boolean
  def setOnOff(state: Boolean): Unit
  def toggle(): Unit
}

trait Colored {
  def setColor(color: Color): Unit
}

class IKEABulb(topic: String)(using mqtt: Mqtt) extends OnOff, Colored {
  // https://www.zigbee2mqtt.io/devices/LED2109G6.html

  import IKEABulb.*
  private var state: State = uninitialized
  private def getState: State =
    if (state==null) throw IllegalStateException("State unknown")
    else state
  mqtt.subscribe(topic, subscriptionListener)
  mqtt.publish(s"$topic/get", """{"state": ""}""")

  private def subscriptionListener(topic: String, payload: String): Unit =
    state = upickle.default.read[IKEABulb.State](payload)

  def setRaw(subtopic: String, value: String): Unit =
    mqtt.publish(if (subtopic != "") s"$topic/set/$subtopic" else s"$topic/set", value)

  def setOnOff(state: Boolean): Unit =
    setRaw("state", if (state) "ON" else "OFF")
  def toggle(): Unit =
    setRaw("state", "TOGGLE")
  override def isOn: Boolean =
    getState.state match
      case "ON" => true
      case "OFF" => false

  def identify(): Unit =
    setRaw("identify", "activate")

  override def setColor(color: Color): Unit = {
    val color2 = ColorRGB(color.getRed, color.getGreen, color.getBlue)
    val json = upickle.default.write(color2)
    mqtt.publish(s"$topic/set/color", json)
  }
}

object IKEABulb {
  private case class State(brightness: Int, state: String) derives ReadWriter
  private case class ColorRGB(r: Int, g: Int, b: Int) derives ReadWriter
}

object Test {
  def main(args: Array[String]): Unit = {
    given Mqtt()
    val bulb = new IKEABulb("zigbee2mqtt/light_bedroom_ceiling")

    bulb.setOnOff(true)
    bulb.setRaw("brightness", "254")
    Thread.sleep(1000)
    bulb.setRaw("", """{"brightness": 0, "transition": 100}""")

//    bulb.toggle()
//    bulb.identify()
//    bulb.setColor(Color.RED)
//    Thread.sleep(1000)
//    println(bulb.isOn)
  }
}

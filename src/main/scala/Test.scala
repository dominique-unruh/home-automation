package de.unruh.homeautomation

import de.unruh.homeautomation.devices.IKEABulb
import de.unruh.homeautomation.interfaces.{Colored, OnOff}
import org.eclipse.paho.mqttv5.client.{IMqttMessageListener, IMqttToken, MqttClient, MqttConnectionOptions}
import org.eclipse.paho.mqttv5.common.{MqttMessage, MqttSubscription}
import upickle.default.ReadWriter

import monix.execution.Scheduler.Implicits.global

import java.awt
import java.awt.Color
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration











object Test {
  def main(args: Array[String]): Unit = {
    given Mqtt()
    val bulb = new IKEABulb("zigbee2mqtt/light_bedroom_ceiling")

    bulb.setOn(true)
    bulb.setRaw("brightness", "254")
    Thread.sleep(1000)
    bulb.setOn(false)
    Thread.sleep(1000)
    println(bulb.isOn.firstL.runSyncUnsafe(Duration.Inf))
    bulb.setOn(true)
    Thread.sleep(1000)
    println(bulb.isOn.firstL.runSyncUnsafe(Duration.Inf))
  }
}

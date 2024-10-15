package de.unruh.homeautomation

import de.unruh.homeautomation.MyDevices.{bedroomCeilingLight, remote1}
import de.unruh.homeautomation.devices.{IKEABulb, RodretRemote}
import de.unruh.homeautomation.interfaces.{Colored, OnOff}
import monix.eval.Task
import monix.execution.Ack
import org.eclipse.paho.mqttv5.client.{IMqttMessageListener, IMqttToken, MqttClient, MqttConnectionOptions}
import org.eclipse.paho.mqttv5.common.{MqttMessage, MqttSubscription}
import upickle.default.ReadWriter
import monix.execution.Scheduler.Implicits.global

import java.awt
import java.awt.Color
import scala.compiletime.uninitialized
import scala.concurrent.Future
import scala.concurrent.duration.Duration











object Test {
  def main(args: Array[String]): Unit = {
    
//    bulb.isOn.subscribe(state => Future { println("S"+state); Ack.Continue })
//    remote.battery.subscribe(state => Future { println("S"+state); Ack.Continue })

//    bulb.setOn(true)

    bedroomCeilingLight.isOn.subscribe(bool => Future {
      if (!bool) bedroomCeilingLight.setOn(true)
      Ack.Continue
    })

    remote1.action.subscribe(action => Future {
      println(s"ACTION:$action")
      Ack.Continue
    })

    //    bulb.isOn.subscribe(b => Future { println(b); Ack.Continue })

//    Thread.sleep(1000)
//    println(bulb.isOn.firstL.runSyncUnsafe(Duration.Inf))




//    implicitly[Mqtt].subscribe("zigbee2mqtt/rodret_remote_1")
//      .dump("Rodret")

//    remote.identify()
//    bulb.identify()
/*
    bulb.setOn(true)
    bulb.setRaw("brightness", "254")
    Thread.sleep(1000)
    bulb.setOn(false)
    Thread.sleep(1000)
    println(bulb.isOn.firstL.runSyncUnsafe(Duration.Inf))
    bulb.setOn(true)
    Thread.sleep(1000)
    println(bulb.isOn.firstL.runSyncUnsafe(Duration.Inf))
*/

    bedroomCeilingLight.colorLoop(true)
  }
}

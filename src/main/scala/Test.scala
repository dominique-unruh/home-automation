package de.unruh.homeautomation

import de.unruh.homeautomation.MyDevices.{bedroomCeilingLight, remote1, windowBedroom}
import de.unruh.homeautomation.devices.{TradfriBulb, RodretRemote}
import de.unruh.homeautomation.interfaces.{Colored, OnOff}
import monix.eval.Task
import monix.execution.Ack
import org.eclipse.paho.mqttv5.client.{IMqttMessageListener, IMqttToken, MqttClient, MqttConnectionOptions}
import org.eclipse.paho.mqttv5.common.{MqttMessage, MqttSubscription}
import upickle.default.ReadWriter
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import java.awt
import java.awt.Color
import scala.compiletime.uninitialized
import scala.concurrent.Future
import scala.concurrent.duration.Duration



object Test {
  def peek[A](observable: Observable[A]): A =
    observable.firstL.runSyncUnsafe(Duration("10s"))

  def main(args: Array[String]): Unit = {

//    bulb.isOn.subscribe(state => Future { println("S"+state); Ack.Continue })
//    remote.battery.subscribe(state => Future { println("S"+state); Ack.Continue })

//    bulb.setOn(true)

//    bedroomCeilingLight.isOn.subscribe(bool => Future {
//      if (!bool) bedroomCeilingLight.setOn(true)
//      Ack.Continue
//    })

    remote1.action.subscribe(action => Future {
      println(s"ACTION:$action")
      Ack.Continue
    })

//    println("id")
//    windowBedroom.identify()
//    println("isOpen?")
//    println(peek(windowBedroom.isOpen))
//    println("battery?")
//    println(peek(windowBedroom.battery))
    
    windowBedroom.isOpen.subscribe(open => Future {
      bedroomCeilingLight.setOn(!open)
      Ack.Continue
    })

    //    bulb.isOn.subscribe(b => Future { println(b); Ack.Continue })

//  bedroomCeilingLight.setRaw("color", """{"hue":0, "saturation":0}""")

/*
    bulb.setRaw("brightness", "254")
*/

//    bedroomCeilingLight.setOn(true)
//    bedroomCeilingLight.colorLoop(false)
//    val desired = Color.RED
//    println(desired)
//    bedroomCeilingLight.setColor(desired)
//    val color = peek(bedroomCeilingLight.color)
//    println(color)
/*
    while (true) {
      Thread.sleep(1000)
      val color = peek(bedroomCeilingLight.color)
      println(color)
      bedroomCeilingLight.setColor(color)
    }
*/

//    bedroomCeilingLight.setColor(Color.BLUE)
//    bedroomCeilingLight.colorLoop(true)
  }
}

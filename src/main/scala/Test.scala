package de.unruh.homeautomation

import MyDevices.{mqtt, *}
import Utils.{onChange, peek}

import monix.execution.{Ack, Cancelable}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import java.awt
import java.awt.Color
import scala.concurrent.Future
import scala.concurrent.duration.Duration



object Test {
  var pingNo = 0
  def ping() = synchronized {
    pingNo += 1
    println(s"Ping: $pingNo")
  }
  
  def main(args: Array[String]): Unit = {

//    val sub = MyDevices.mqtt.subscribe("SleepAsAndroid")

    onChange(sleepAsAndroid.alarmRescheduled, println)

//    onChange(sub, println)

//    MyDevices.floodlightTop.colorLoop(true)

//    darknessCozyroom()

/*

    onChange(windowBedroom.isOpen, { open =>
      if (open) {
        println(s"Bedroom window open, opening blinds")
        blindsBedroom.setPosition(1)
      }})
*/

//    blindsBedroom.setPosition(0.9)


/*

    bedroomCeilingLight.setOn(true)

    ping()

    onChange(remote1.action,
      action => println(s"ACTION:$action"))

    ping()

    onChange(windowBedroom.isOpen, { open =>
      println(s"Setting light ${!open}")
      bedroomCeilingLight.setOn(!open)
    })

    ping()

    onChange(bedroomCeilingLight.colorTemperature,
      t => println(s"Color temp: ${t}"))

    ping()
    bedroomCeilingLight.setOn(true)
    ping()
    bedroomCeilingLight.colorLoop(true)
    ping()
*/

//    bedroomCeilingLight.setBrightness(1)
//    bedroomCeilingLight.setColorTemperature(3154)
//    Thread.sleep(1000)
//    println(peek(bedroomCeilingLight.colorTemperature))
//    bedroomCeilingLight.setColor(Color.blue)
  }
}

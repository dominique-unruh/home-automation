package de.unruh.homeautomation

import MyDevices.*
import Utils.{onChange, peek}

import monix.execution.{Ack, Cancelable}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import java.awt
import java.awt.Color
import scala.concurrent.Future
import scala.concurrent.duration.Duration



object Test {
  def darknessCozyroom(): Unit = {
    floodlightTop.setOn(false)
    floodlightSide.setOn(false)
    varmblixt.setOn(false)
  }
  
  def darknessBedroom(): Unit = {
    bedroomCeilingLight.setOn(false)
  }
  
  def darknessLivingroom(): Unit = {
    livingroomCeilingLightLeft.setOn(false)
    livingroomCeilingLightRight.setOn(false)
  }
  
  
  var pingNo = 0
  def ping() = synchronized {
    pingNo += 1
    println(s"Ping: $pingNo")
  }
  
  def main(args: Array[String]): Unit = {
    ping()

//    MyDevices.floodlightTop.colorLoop(true)

//    darknessCozyroom()


    onChange(windowBedroom.isOpen, { open =>
      if (open) {
        println(s"Bedroom window open, activating light")
        bedroomCeilingLight.setOn(true)
      }})


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

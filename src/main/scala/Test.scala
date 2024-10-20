package de.unruh.homeautomation

import MyDevices.{bedroomCeilingLight, remote1, windowBedroom}
import Utils.{onChange, peek}

import monix.execution.{Ack, Cancelable}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import java.awt
import java.awt.Color
import scala.concurrent.Future
import scala.concurrent.duration.Duration



object Test {
  def main(args: Array[String]): Unit = {
    onChange(remote1.action,
      action => println(s"ACTION:$action"))

    onChange(windowBedroom.isOpen, { open =>
      println(s"Setting light ${!open}")
      bedroomCeilingLight.setOn(!open)
    })

    onChange(bedroomCeilingLight.colorTemperature,
      t => println(s"Color temp: ${t}"))

    bedroomCeilingLight.setOn(true)
    bedroomCeilingLight.colorLoop(true)
//    bedroomCeilingLight.setBrightness(1)
//    bedroomCeilingLight.setColorTemperature(3154)
//    Thread.sleep(1000)
//    println(peek(bedroomCeilingLight.colorTemperature))
//    bedroomCeilingLight.setColor(Color.blue)
  }
}

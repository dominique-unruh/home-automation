package de.unruh.homeautomation

import MyDevices.{mqtt, *}
import Utils.{onChange, peek}

import de.unruh.homeautomation.controller.{Controller, ControllerBase}



object Test {
  private var pingNo = 0
  def ping(): Unit = synchronized {
    pingNo += 1
    println(s"Ping: $pingNo")
  }
  
  def main(args: Array[String]): Unit = {

    Controller.run()

/*    onChange(heatingBedroom.battery, println)

    onChange(heatingBedroom.targetTemperature, println)
    onChange(heatingBedroom.currentTemperature, println)

    Thread.sleep(1000)
    heatingBedroom.setTargetTemperature(18)*/
  }
}

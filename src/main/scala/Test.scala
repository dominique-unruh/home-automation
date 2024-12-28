package de.unruh.homeautomation

import MyDevices.{mqtt, *}
import Utils.{onChange, peek}



object Test {
  private var pingNo = 0
  def ping(): Unit = synchronized {
    pingNo += 1
    println(s"Ping: $pingNo")
  }
  
  def main(args: Array[String]): Unit = {

    onChange(heatingBedroom.battery, println)

    onChange(heatingBedroom.targetTemperature, println)
    onChange(heatingBedroom.currentTemperature, println)

    Thread.sleep(1000)
    heatingBedroom.setTargetTemperature(19)
  }
}

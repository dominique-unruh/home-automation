package de.unruh.homeautomation
package controller

import Utils.onChange
import MyDevices.*

import scala.concurrent.duration.Duration

//noinspection TypeAnnotation
object Controller extends ControllerHelpers {

  /** Switch bedroom light to random color whenever activated */
  def bedroomLightRandomColor = {
    onChange(bedroomCeilingLight.isOn, Utils.atMostEvery(Duration("10s"),
      code = _ => {
        println("Bedroom light status change ==> changing color")
        bedroomCeilingLight.setColor(Utils.randomColor)
      },
      filter = isOn => isOn
    ))
  }

  /** When setting an alarm, put the flat in sleep mode */
  def sleepWhenAlarmActivated = {
    onChange(sleepAsAndroid.alarmRescheduled,
      event => if (event.activated) {
        println("Alarm scheduled ==> putting flat to sleep")
        sleep()
      })
  }

  /** When the alarm rings, open the blinds */
  def alarmRingsThenOpenBlinds = {
  onChange(sleepAsAndroid.alarmStart, event => {
    println("Alarm started ==> opening blinds")
      blindsBedroom.setPosition(1)
    })
  }
  
}

package de.unruh.homeautomation
package devices

import upickle.default.ReadWriter
import interfaces.{Battery, Identify, OpenClosed}

import monix.reactive.Observable

/** https://www.zigbee2mqtt.io/devices/E2013.html */
class ParasollDoorWindowSensor(topic: String)(using Mqtt)
  extends Zigbee2Mqtt[ParasollDoorWindowSensor.State](topic, "battery"), Identify, OpenClosed, Battery {
  override def identify(): Unit =
    setRaw("identify", "identify")

  override val isOpen: Observable[Boolean] =
    state.map { s =>
      s.contact match {
        case "true" => false
        case "false" => true
      }
    }
    
  override val battery: Observable[Double] =
    state.map(_.battery / 100.0)
}

object ParasollDoorWindowSensor {
  protected case class State(contact: String, battery: Int) derives ReadWriter
}
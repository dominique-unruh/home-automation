package de.unruh.homeautomation
package devices

import interfaces.{Identify, OnOff}

import monix.reactive.Observable
import upickle.default.ReadWriter

import java.time.Duration

class TretaktPlug(topic: String)(implicit mqtt: Mqtt) extends
  Zigbee2Mqtt[TretaktPlug.State](topic, "state"), Identify, OnOff {

  override def setOn(state: Boolean): Unit =
    setRaw("state", if (state) "ON" else "OFF")

  def setOnTemporary(duration: Duration): Unit =
    setRaw("", s"""{"state": "ON", "on_time": ${duration.getSeconds}}""")

  override def toggle(): Unit =
    setRaw("state", "TOGGLE")

  override lazy val isOn: Observable[Boolean] =
    state.map { _.state match {
        case "ON" => true
        case "OFF" => false
      }
    }

  override def identify(): Unit =
    setRaw("identify", "identify")
}

object TretaktPlug {
  case class State(state: String) derives ReadWriter
}
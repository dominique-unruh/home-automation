package de.unruh.homeautomation
package devices

import devices.TradfriBulb.*
import interfaces.{Battery, Blind, Brightness, ColorTemperature, Colored, Identify, OnOff, OpenClosed}

import monix.execution.Cancelable
import monix.execution.ChannelType.MultiProducer
import monix.execution.Scheduler.Implicits.global
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.BehaviorSubject
import monix.reactive.{Observable, OverflowStrategy}
import upickle.default.{ReadWriter, read}

import java.awt.Color
import java.awt.color.ColorSpace
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

/** https://www.zigbee2mqtt.io/devices/E2103.html */
class TredansenBlind(topic: String, window: OpenClosed)(using mqtt: Mqtt)
  extends Zigbee2Mqtt[TredansenBlind.State](topic, "state"),
    Identify, Blind, Battery {

  private var windowOpen = false

  Utils.onChange(window.isOpen, isOpen =>
    if (isOpen)
      setPosition(1)
    windowOpen = isOpen
  )

  override def stopPositionChange(): Unit =
    setRaw("state", "STOP")

  override def setPosition(position: Double): Unit = {
    if (position >= 1 || windowOpen)
      setRaw("state", "OPEN")
    else if (position <= 0)
      setRaw("state", "CLOSE")
    else
      setRaw("position", (position * 100).toInt.toString)
  }

  override lazy val position: Observable[Double] =
    state.map { s => s.position / 100.0 }

  override def identify(): Unit =
    setRaw("identify", "identify")

  override val battery: Observable[Double] =
    state.map(_.battery / 100.0)
}

object TredansenBlind {
  protected case class State(state: String, position: Int, battery: Int) derives ReadWriter
}
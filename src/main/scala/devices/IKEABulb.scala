package de.unruh.homeautomation
package devices

import IKEABulb.*
import interfaces.{Colored, Identify, OnOff}

import monix.execution.Cancelable
import monix.execution.ChannelType.MultiProducer
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.BehaviorSubject
import monix.reactive.{Observable, OverflowStrategy}
import upickle.default.{ReadWriter, read}
import monix.execution.Scheduler.Implicits.global

import java.awt.Color
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

/** https://www.zigbee2mqtt.io/devices/LED2109G6.html **/
class IKEABulb(topic: String)(using mqtt: Mqtt)
  extends Zigbee2Mqtt[IKEABulb.State](topic, "state"),
    OnOff, Colored, Identify {

  override def setOn(state: Boolean): Unit =
    setRaw("state", if (state) "ON" else "OFF")

  override def toggle(): Unit =
    setRaw("state", "TOGGLE")

  override val isOn: Observable[Boolean] =
    state.map { s => s.state match {
      case "ON" => true
      case "OFF" => false
    }}

  override def identify(): Unit =
    setRaw("identify", "identify")

  override def setColor(color: Color): Unit = {
    val color2 = ColorRGB(color.getRed, color.getGreen, color.getBlue)
    val json = upickle.default.write(color2)
    mqtt.publish(s"$topic/set/color", json)
  }

  def colorLoop(activate: Boolean) =
    if (activate)
      setRaw("effect", "colorloop")
    else
      setRaw("effect", "colorloop_stop")
}

object IKEABulb {
  protected case class State(brightness: Int, state: String) derives ReadWriter
  private case class ColorRGB(r: Int, g: Int, b: Int) derives ReadWriter
}
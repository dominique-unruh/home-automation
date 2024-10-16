package de.unruh.homeautomation
package devices

import TradfriBulb.*
import interfaces.{Colored, Identify, OnOff}

import monix.execution.Cancelable
import monix.execution.ChannelType.MultiProducer
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.BehaviorSubject
import monix.reactive.{Observable, OverflowStrategy}
import upickle.default.{ReadWriter, read}
import monix.execution.Scheduler.Implicits.global

import java.awt.Color
import java.awt.color.ColorSpace
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

/** https://www.zigbee2mqtt.io/devices/LED2109G6.html **/
class TradfriBulb(topic: String)(using mqtt: Mqtt)
  extends Zigbee2Mqtt[TradfriBulb.State](topic, "state"),
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
/*
    val color2 = ColorRGB(color.getRed, color.getGreen, color.getBlue)
    val json = upickle.default.write(color2)
    mqtt.publish(s"$topic/set/color", json)
*/

        val xyz = xyzColorSpace.fromRGB(color.getRGBComponents(null))
        val sum = xyz(0) + xyz(1) + xyz(2)
        val x = xyz(0) / sum
        val y = xyz(1) / sum
        val color2 = ColorXY(x,y)
        println(color2)
        val json = upickle.default.write(color2)
        mqtt.publish(s"$topic/set/color", json)

  }

  override val color: Observable[Color] =
    state.map { s =>
//      Color.getHSBColor(s.color.hue / 360f, s.color.saturation / 100f, .3f)
      val Y = .2f
      val z = 1 - s.color.x - s.color.y
      val Array(r,g,b) = xyzColorSpace.toRGB(Array(Y * s.color.x, Y * s.color.y, Y * z))
      val max = math.max(r, math.max(g, b))
      if (max == 0)
        Color.black
      else
        Color(r / max, g / max, b / max)
    }

  def colorLoop(activate: Boolean): Unit =
    if (activate)
      setRaw("effect", "colorloop")
    else
      setRaw("effect", "colorloop_stop")
}

object TradfriBulb {
  protected case class State(brightness: Int, state: String, color: ColorXY) derives ReadWriter
  protected case class ColorXY(x: Float, y: Float) derives ReadWriter
  protected case class ColorHS(hue: Int, saturation: Int) derives ReadWriter
  private case class ColorRGB(r: Int, g: Int, b: Int) derives ReadWriter
  private val xyzColorSpace = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ)
}
package de.unruh.homeautomation
package devices

import interfaces.{Action, Battery, Identify}

import monix.execution.Ack
import upickle.default.ReadWriter
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import RodretRemote.Action.*

import scala.concurrent.Future

/** https://www.zigbee2mqtt.io/devices/E2201.html */
class RodretRemote(topic: String)(implicit mqtt: Mqtt)
  extends Zigbee2Mqtt[RodretRemote.State](topic, "battery"), Identify, Battery, Action[RodretRemote.Action] {

  override val action: Observable[RodretRemote.Action] = state.collect({ (state:RodretRemote.State) => state.action match
    case "" => None
    case null => None
    case "on" => Some(On)
    case "off" => Some(Off)
    case "brightness_move_up" => Some(BrightnessMoveUp)
    case "brightness_move_down" => Some(BrightnessMoveDown)
    case "brightness_stop" => Some(BrightnessStop)
    case action => println(s"Unexpected action: ${action}"); None
  }.unlift)

  override val battery: Observable[Double] =
    state.map(_.battery / 100.0)

  override def identify(): Unit =
    setRaw("identify", "identify")
}

object RodretRemote {
  protected case class State(action: String, battery: Int) derives ReadWriter

  enum Action {
    case On, Off, BrightnessMoveUp, BrightnessMoveDown, BrightnessStop
  }
}
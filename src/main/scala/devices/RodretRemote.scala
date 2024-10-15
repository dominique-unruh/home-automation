package de.unruh.homeautomation
package devices

import de.unruh.homeautomation.devices.RodretRemote.Action
import de.unruh.homeautomation.interfaces.{Battery, Identify}
import monix.execution.Ack
import upickle.default.ReadWriter
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import scala.concurrent.Future

/** https://www.zigbee2mqtt.io/devices/E2201.html */
class RodretRemote(topic: String)(implicit mqtt: Mqtt)
  extends Zigbee2Mqtt[RodretRemote.State](topic, "battery"), Identify, Battery {

  // TODO action trait
  val action = state.collect({ (state:RodretRemote.State) => state.action match
    case "" => None
    case null => None
    case "on" => Some(Action.On)
    case "off" => Some(Action.Off)
    case "brightness_move_up" => Some(Action.BrightnessMoveUp)
    case "brightness_move_down" => Some(Action.BrightnessMoveDown)
    case "brightness_stop" => Some(Action.BrightnessStop)
    case action => println(s"Unexpected action: ${action}"); None
  }.unlift)

  override val battery: Observable[Int] =
    state.map(_.battery)

  override def identify(): Unit =
    setRaw("identify", "identify")
}

object RodretRemote {
  protected case class State(action: String, battery: Int) derives ReadWriter
  
  enum Action {
    case On, Off, BrightnessMoveUp, BrightnessMoveDown, BrightnessStop 
  }
}
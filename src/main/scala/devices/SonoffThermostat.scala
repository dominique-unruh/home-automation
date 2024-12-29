package de.unruh.homeautomation
package devices

import devices.TradfriBulb.*
import interfaces.{Battery, Brightness, ColorTemperature, Colored, Identify, OnOff, Thermostat}

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

/** https://www.zigbee2mqtt.io/devices/TRVZB.html */
class SonoffThermostat(topic: String)(using mqtt: Mqtt)
  extends Zigbee2Mqtt[SonoffThermostat.State](topic, "state"),
    Battery, Thermostat {

  Utils.onChange(state, println)

  override val battery: Observable[Double] =
    state.map(_.battery / 100.0)

  override lazy val targetTemperature: Observable[Double] =
    state.map(_.occupied_heating_setpoint)

  override def setTargetTemperature(temperature: Double): Unit =
    setRaw("occupied_heating_setpoint", temperature.toString)

  override lazy val currentTemperature: Observable[Double] =
    state.map(_.local_temperature)

}

object SonoffThermostat {
  protected enum SystemMode derives ReadWriter { case off, auto, heat}
  protected enum RunningState derives ReadWriter { case idle, heat }
  protected enum LockUnlock derives ReadWriter { case LOCK, UNLOCK }
  protected enum OnOff derives ReadWriter { case ON, OFF }
  protected case class State(
                              occupied_heating_setpoint: Double,
                              local_temperature: Double,
                              system_mode: SystemMode,
                              running_state: RunningState,
                              local_temperature_calibration: Double,
                              child_lock: LockUnlock,
                              open_window: OnOff,
                              frost_protection_temperature: Double,
                              battery: Int,
                            ) derives ReadWriter
}

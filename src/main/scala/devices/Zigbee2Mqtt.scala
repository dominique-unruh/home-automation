package de.unruh.homeautomation
package devices

import devices.TradfriBulb.State

import monix.reactive.Observable
import monix.reactive.subjects.BehaviorSubject
import upickle.default.{Reader, read}

import monix.execution.Scheduler.Implicits.global

/** Standard implementation for Zigbee2Mqtt devices.
 * @param topic The Mqtt topic of this device
 * @param stateTrigger A keyword xxx so that sending `{"xxx": ""}` to `topic/get`
 *                     will trigger that the device sends its state
 *                     (usually one of the field namess of the state JSON record,
 *                     e.g., `state`, `battery`, ...).
 * */
trait Zigbee2Mqtt[State >: Null : Reader](topic: String,
                                          stateTrigger: String)(implicit mqtt: Mqtt) {
  protected val state: Observable[State] = {
    val subject = BehaviorSubject[State](null)
    mqtt.subscribe(topic)
      .map((topic, message) => read[State](message.getPayload))
      .subscribe(subject)
    mqtt.publish(s"$topic/get", s"""{"$stateTrigger": ""}""")
    subject.filterNot(_ == null)
  }

  def setRaw(subtopic: String, value: String): Unit =
    mqtt.publish(if (subtopic != "") s"$topic/set/$subtopic" else s"$topic/set", value)
}

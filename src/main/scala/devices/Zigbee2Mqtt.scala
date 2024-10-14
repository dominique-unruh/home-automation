package de.unruh.homeautomation
package devices

import devices.IKEABulb.State

import monix.reactive.Observable
import monix.reactive.subjects.BehaviorSubject
import upickle.default.{Reader, read}

import monix.execution.Scheduler.Implicits.global

trait Zigbee2Mqtt[State >: Null : Reader](topic: String)(implicit mqtt: Mqtt) {
  protected val state: Observable[State] = {
    val subject = BehaviorSubject[State](null)
    mqtt.subscribe(topic)
      .map((topic, message) => read[State](message.getPayload))
      .subscribe(subject)
    mqtt.publish(s"$topic/get", """{"state": ""}""")
    subject
  }

  def setRaw(subtopic: String, value: String): Unit =
    mqtt.publish(if (subtopic != "") s"$topic/set/$subtopic" else s"$topic/set", value)
}

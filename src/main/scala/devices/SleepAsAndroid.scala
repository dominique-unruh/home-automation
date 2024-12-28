package de.unruh.homeautomation
package devices

import devices.SleepAsAndroid.{AlarmDismissEvent, AlarmRescheduledEvent, AlarmStartEvent, Message}

import de.unruh.homeautomation.Utils.ImperativeObservable
import monix.execution.ChannelType.MultiProducer
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.subjects.BehaviorSubject
import upickle.default.{ReadWriter, Reader, read}
import monix.execution.Scheduler.Implicits.global
import org.reactivestreams.Publisher

object SleepAsAndroid {
  private val imperativeObservable = ImperativeObservable[Message]()

  def incomingWebhook(json: String): Unit = {
    println(s"Sleep as android webhook: ${json}")
    val message = read[Message](json)
    imperativeObservable.publish(message)
  }

  val state: Observable[Message] = imperativeObservable.observable

  Utils.onChange(state, println)

  val alarmRescheduled: Observable[AlarmRescheduledEvent] = state
    .filter(_.event == "alarm_rescheduled")
    .map { event => AlarmRescheduledEvent(event.value1.map(_.toLong)) }

  val alarmStart: Observable[AlarmStartEvent] = state
    .filter(_.event == "alarm_alert_start")
    .map { event => AlarmStartEvent(event.value1.get.toLong) }

  val alarmDismiss: Observable[AlarmDismissEvent] = state
    .filter(_.event == "alarm_alert_dismiss")
    .map { event => AlarmDismissEvent(event.value1.get.toLong) }

  private[SleepAsAndroid] case class Message(event: String, value1: Option[String] = None, value2: Option[String] = None) derives ReadWriter
  case class AlarmRescheduledEvent(time: Option[Long]) {
    def activated: Boolean = time.nonEmpty
  }
  case class AlarmStartEvent(time: Long)
  case class AlarmDismissEvent(time: Long)

}


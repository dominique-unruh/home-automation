package de.unruh.homeautomation

import monix.execution.Ack.Stop
import monix.execution.Cancelable
import monix.execution.ChannelType.MultiProducer
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.observers.Subscriber
import org.eclipse.paho.mqttv5.client.{IMqttMessageListener, IMqttToken, MqttClient, MqttConnectionOptions}
import org.eclipse.paho.mqttv5.common.{MqttMessage, MqttSubscription}

import java.util

class Mqtt {
  private val subscriptions = util.HashSet[String]()
  private val client = {
    val clientId = "de.unruh.homeautomation"
    val client = new MqttClient("tcp://quietscheentchen:1883", clientId)
    val options = new MqttConnectionOptions()
    options.setAutomaticReconnect(true)
    options.setConnectionTimeout(10)
    client.connect(options)
    client
  }

//  subscribe("#", (topic, msg) => println(s"Received @ $topic: $msg")) // Logging

  def publish(topic: String, payload: String): Unit = {
    client.publish(topic, payload.getBytes, 0, false)
  }

  def subscribe(topic: String): Observable[(String, MqttMessage)] = {
    assert(!subscriptions.contains(topic))
    subscriptions.add(topic)
    def f(subscriber: Subscriber.Sync[(String, MqttMessage)]): Cancelable = {
      val subscriptionToken = client.subscribe(
        Array(MqttSubscription(topic)),
        Array[IMqttMessageListener]((topic: String, message: MqttMessage) => {
          val response = subscriber.onNext((topic, message))
          if (response == Stop)
            {} // No way to unsubscribe?
        }))
      () => () // No way to unsubscribe?
    }
    Observable.create(OverflowStrategy.DropOld(2), MultiProducer)(f)
  }

/*  def subscribe(topic: String, callback: (String, String) => Unit): IMqttToken = {
    client.subscribe(
      Array(MqttSubscription(topic)),
      Array[IMqttMessageListener]((topic: String, message: MqttMessage) => {
        try {
          callback(topic, String(message.getPayload))
        } catch
          case e: Throwable =>
            e.printStackTrace()
      }))
  }*/
}

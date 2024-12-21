package de.unruh.homeautomation

import monix.execution.Ack.Stop
import monix.execution.Cancelable
import monix.execution.ChannelType.MultiProducer
import monix.reactive.{Observable, OverflowStrategy}
import monix.reactive.observers.Subscriber
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttConnectOptions, MqttMessage}

import java.io.{BufferedReader, FileInputStream, FileReader}
import java.util
import scala.io.BufferedSource
import scala.util.Using

class Mqtt {
  private val subscriptions = util.HashSet[String]()
  private val client = {
    val clientId = "de.unruh.homeautomation"
    val client = new MqttClient("tcp://djinn:1883", clientId)
    val options = new MqttConnectOptions()
    options.setAutomaticReconnect(true)
    options.setConnectionTimeout(10)
    val Seq(username, password) =
      Using(new BufferedSource(new FileInputStream(".mqtt-credentials.secret"))) {
        source => source.getLines()
      }.get.toSeq
    options.setUserName(username)
    options.setPassword(password.toCharArray)
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
    def unsubscribe(): Unit = client.unsubscribe(topic)
    def f(subscriber: Subscriber.Sync[(String, MqttMessage)]): Cancelable = {
      client.subscribe(
        topic,
        (topic: String, message: MqttMessage) => {
          val response = subscriber.onNext((topic, message))
          if (response == Stop)
            unsubscribe()
        })
      () => unsubscribe()
    }
    Observable.create(OverflowStrategy.DropOld(2), MultiProducer)(f)
  }
}

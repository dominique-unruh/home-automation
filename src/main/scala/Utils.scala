package de.unruh.homeautomation

import monix.execution.ChannelType.MultiProducer
import monix.execution.{Ack, Cancelable}
import monix.reactive.{Observable, OverflowStrategy}

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import monix.execution.Scheduler.Implicits.global
import monix.reactive.observers.Subscriber
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.reactivestreams.Publisher

import java.awt.Color
import java.io.{PrintWriter, StringWriter}
import java.util.concurrent.{ConcurrentLinkedDeque, TimeoutException}
import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.compiletime.uninitialized
import scala.util.Random

object Utils {
  def peek[A](observable: Observable[A], duration: Duration = Duration("10s")): A =
    observable.firstL.runSyncUnsafe(duration)
  def peekOption[A](observable: Observable[A], duration: Duration = Duration("10s")): Option[A] =
    try
      Some(peek(observable, duration))
    catch
      case _: NoSuchElementException => None
      case _: TimeoutException => None


  def onChange[A](observable: Observable[A], callback: A => Unit): Cancelable =
    observable.subscribe(a => Future {
      try {
        callback(a)
      } catch {
        case e: Throwable => e.printStackTrace()
      }
      Ack.Continue
    })

  def randomColor: Color =
    Color.getHSBColor(Random.nextFloat, 1, 1)

  def stackTrace(e: Throwable): String = {
    val writer = StringWriter()
    e.printStackTrace(PrintWriter(writer))
    writer.toString
  }

  /** Returns a function that executes `code`, but at most every `duration` time.
   * Invocations with `filter(...)=true` are ignored (i.e., do not affect the timing.
   * */
  def atMostEvery[A](duration: Duration, code: A => Unit, filter: A => Boolean = {(_:A) => true}) : A => Unit = {
    var lastExec: Long = -1
    arg => {
      val now = System.currentTimeMillis()
      if (duration.toMillis + lastExec <= now) {
        if (filter(arg)) {
          lastExec = now
          code(arg)
        }
      }
    }
  }

  class ImperativeObservable[A] {
    private val subscribers = ConcurrentLinkedDeque[Subscriber.Sync[A]]()
    val observable: Observable[A] = Observable.create(OverflowStrategy.DropOld(2), MultiProducer) { subscriber =>
      println(s"Adding subscriber $subscriber")
      subscribers.add(subscriber)
      () => subscribers.remove(subscriber)
    }

    def publish(message: A): Unit = 
      for (subscriber <- subscribers.iterator.asScala)
        subscriber.onNext(message)
  }
}
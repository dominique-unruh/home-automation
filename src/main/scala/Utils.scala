package de.unruh.homeautomation

import monix.execution.{Ack, Cancelable}
import monix.reactive.Observable

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import monix.execution.Scheduler.Implicits.global

import java.awt.Color
import java.io.{PrintWriter, StringWriter}
import java.util.concurrent.TimeoutException
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
}
package de.unruh.homeautomation

import monix.execution.{Ack, Cancelable}
import monix.reactive.Observable

import scala.concurrent.Future
import scala.concurrent.duration.Duration

import monix.execution.Scheduler.Implicits.global

object Utils {
  def peek[A](observable: Observable[A], duration: Duration = Duration("10s")): A =
    observable.firstL.runSyncUnsafe(duration)
  def onChange[A](observable: Observable[A], callback: A => Unit): Cancelable =
    observable.subscribe(a => Future { callback(a); Ack.Continue })
}
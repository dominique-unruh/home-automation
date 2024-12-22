package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Blind {
  /** Position between 0 and 1. (0=closed, 1=open) */
  lazy val position: Observable[Double]
  def setPosition(position: Double): Unit
  /** Stops the current position change */
  def stopPositionChange(): Unit
}

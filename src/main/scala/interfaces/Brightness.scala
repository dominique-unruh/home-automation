package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Brightness {
  def setBrightness(brightness: Double): Unit
  lazy val brightness: Observable[Double]
}

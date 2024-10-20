package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait ColorTemperature {
  /** Set the color temperature in Kelvin */
  def setColorTemperature(temperature: Double): Unit
  /** The color temperature in Kelvin */
  lazy val colorTemperature: Observable[Double]
}

object ColorTemperature {
  def kelvinToMired(kelvin: Double): Double = 1000000 / kelvin
  def miredToKelvin(mired: Double): Double = 1000000 / mired
}
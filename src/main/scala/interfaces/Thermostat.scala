package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Thermostat {
  def targetTemperature: Observable[Double]
  def setTargetTemperature(temperature: Double): Unit
  def currentTemperature: Observable[Double]
}

package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait OnOff {
  lazy val isOn: Observable[Boolean]
  def setOn(state: Boolean): Unit
  def toggle(): Unit
}

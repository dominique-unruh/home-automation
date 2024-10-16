package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

import java.awt.Color

trait Colored {
  def setColor(color: Color): Unit
  val color : Observable[Color]
}

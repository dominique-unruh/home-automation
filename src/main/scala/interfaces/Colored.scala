package de.unruh.homeautomation
package interfaces

import java.awt.Color

trait Colored {
  def setColor(color: Color): Unit
  // TODO: val color : Observable[Color]
}

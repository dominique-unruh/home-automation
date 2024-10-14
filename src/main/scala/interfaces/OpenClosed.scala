package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait OpenClosed {
  val isOpen: Observable[Boolean]
}

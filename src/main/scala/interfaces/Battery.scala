package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Battery {
  /** State of the battery, in the range 0...1 (1 means full) */
  val battery: Observable[Double]
}

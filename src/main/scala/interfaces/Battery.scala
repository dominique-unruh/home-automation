package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Battery {
  val battery: Observable[Int]
}

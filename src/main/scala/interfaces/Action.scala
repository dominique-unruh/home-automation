package de.unruh.homeautomation
package interfaces

import monix.reactive.Observable

trait Action[T] {
  val action: Observable[T]
}

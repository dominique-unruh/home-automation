package de.unruh.homeautomation
package webapp

import org.scalatra.{LifeCycle, ScalatraServlet}
import jakarta.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext): Unit =
    context.mount(new Webapp, "/home")
}
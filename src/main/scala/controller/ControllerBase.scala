package de.unruh.homeautomation
package controller

import controller.ControllerBase.running

import scala.runtime.BoxedUnit

abstract class ControllerBase extends Runnable {
  protected def assertRunning(): Unit =
    assert(running)

  override def run(): Unit = {
    synchronized {
      if (running)
        throw RuntimeException("Controller already running")
      running = true
    }

    try {
      for (method <- getClass.getDeclaredMethods;
           if !method.getName.contains('$')
           if method.getName != "writeReplace") {
        if (method.getParameterCount != 0)
          throw RuntimeException(s"Controller ($getClass) has method (${method.getName}) with non-empty parameter list.")
//        if (method.getReturnType != classOf[Unit])
//          throw RuntimeException(s"Controller ($getClass) has method (${method.getName}) with non-Unit return type ${method.getReturnType}.")
        println(s"Invoking ${method.getName} in Controller")
        method.invoke(this)
      }
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        throw e
    }
  }

  def startAsThread(): Unit = {
    val thread = new Thread(this, "controller")
    thread.start()
  }
}

object ControllerBase {
  private var running = false
}
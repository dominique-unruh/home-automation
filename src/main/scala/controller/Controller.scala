package de.unruh.homeautomation
package controller

import Utils.onChange
import MyDevices.*


object Controller extends Runnable {
  private var running = false

  private def assertRunning(): Unit =
    assert(running)

  override def run(): Unit = {
    synchronized {
      if (running)
        throw RuntimeException("Controller already running")
      running = true
    }

    try {
      println("Controller started")
      code()
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        throw e
    } finally {
      println("Controller exited")
      running = false
    }
  }

  private def code(): Unit = {
    onChange(windowBedroom.isOpen, { open =>
      if (open) {
        println(s"Bedroom window open, activating lights")
        bedroomCeilingLight.setOn(true)
      }
    })

    onChange(bedroomCeilingLight.isOn, { on =>
      println(s"Bedroom light: $on")
      Console.err.println(s"Bedroom light: $on")
    })

    Thread.sleep(10000000)
  }

  def startAsThread() : Unit = {
    val thread = new Thread(this, "controller")
    thread.run()
  }
}

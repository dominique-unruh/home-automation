package de.unruh.homeautomation
package controller

import Utils.onChange
import MyDevices.*


object Controller extends Runnable {
  private var running = false

  private def setRunning(): Unit = synchronized {
    if (running)
      throw RuntimeException("Controller already running")
      running = true
  }

  private def assertRunning(): Unit =
    assert(running)

  override def run(): Unit = {
    setRunning()

    onChange(windowBedroom.isOpen, { open =>
      if (open) {
        println(s"Bedroom window open, activating lights")
        bedroomCeilingLight.setOn(true)
      }
    })

  }

  def startAsThread() : Unit = {
    val thread = new Thread(this, "controller")
    thread.run()
  }
}

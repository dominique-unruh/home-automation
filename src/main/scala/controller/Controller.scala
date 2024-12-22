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
    }
  }

  def startAsThread() : Unit = {
    val thread = new Thread(this, "controller")
    thread.start()
  }

  private def code(): Unit = {
    onChange(windowBedroom.isOpen, { open =>
      if (open) {
        println(s"Bedroom window open, activating lights")
        bedroomCeilingLight.setOn(true)
      }
    })
  }

  def darknessCozyroom(): Unit = {
    assertRunning()
    floodlightTop.setOn(false)
    floodlightSide.setOn(false)
    varmblixt.setOn(false)
  }

  def darknessBedroom(): Unit = {
    assertRunning()
    bedroomCeilingLight.setOn(false)
  }

  def darknessLivingroom(): Unit = {
    assertRunning()
    livingroomCeilingLightLeft.setOn(false)
    livingroomCeilingLightRight.setOn(false)
  }

  def darkness(): Unit = {
    darknessBedroom()
    darknessCozyroom()
    darknessLivingroom()
  }
}

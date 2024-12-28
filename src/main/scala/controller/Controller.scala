package de.unruh.homeautomation
package controller

import Utils.{onChange, peek, peekOption}
import MyDevices.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.xml.Node


object Controller extends Runnable {
  private def code(): Unit = {
    // Switch bedroom light to random color whenever activated
    onChange(bedroomCeilingLight.isOn, Utils.atMostEvery(Duration("10s"),
      code = _ => {
        println("Bedroom light status change ==> changing color")
        bedroomCeilingLight.setColor(Utils.randomColor)},
      filter = isOn => isOn
    ))

    // When setting an alarm, put the flat in sleep mode
    onChange(sleepAsAndroid.alarmRescheduled,
      event => if (event.activated) {
        println("Alarm scheduled ==> putting flat to sleep")
        sleep()
      })

    // When the alarm rings, open the blinds
    onChange(sleepAsAndroid.alarmStart, event => {
      println("Alarm started ==> opening blinds")
      blindsBedroom.setPosition(1)
    })
  }

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

  def darknessCozyroom(): Unit = {
    assertRunning()
    floodlightTop.setOn(false)
    floodlightSide.setOn(false)
    varmblixt.setOn(false)
  }

  def lightCozyroom(): Unit = {
    assertRunning()
    floodlightTop.setOn(true)
    floodlightTop.setColor(Utils.randomColor)
    floodlightSide.setOn(true)
    varmblixt.setOn(true)
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

  def sleep() : Unit = {
    darkness()
    blindsBedroom.setPosition(0)
  }

  def leave(): Seq[Node] = {
    val problems = ListBuffer[xml.Node]()
    try {
      darkness()
      for (window <- windows) {
        if (peekOption(window.isOpen, Duration("1s")).getOrElse(false))
          problems.append(xml.Text(s"${window.description} is open."))
      }
    } catch {
      case e: Throwable =>
        problems.append(<pre>{Utils.stackTrace(e)}</pre>)
    }
    problems.toSeq
  }
}

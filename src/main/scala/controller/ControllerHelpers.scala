package de.unruh.homeautomation
package controller

import MyDevices.{bedroomCeilingLight, blindsBedroom, floodlightSide, floodlightTop, heatingBedroom, livingroomCeilingLightLeft, livingroomCeilingLightRight, varmblixt, windows}
import Utils.peekOption
import controller.Controller.assertRunning

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.xml.Node

abstract class ControllerHelpers extends ControllerBase {
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
    allHeatingsTo(16)
  }

  def allHeatingsTo(temperature: Double): Unit = {
    heatingBedroom.setTargetTemperature(temperature)
  }

  def leave(): Seq[Node] = {
    val problems = ListBuffer[xml.Node]()
    try {
      darkness()
      for (window <- windows) {
        if (peekOption(window.isOpen, Duration("1s")).getOrElse(false))
          problems.append(xml.Text(s"${window.description} is open."))
      }
      allHeatingsTo(16)
    } catch {
      case e: Throwable =>
        problems.append(<pre>{Utils.stackTrace(e)}</pre>)
    }
    problems.toSeq
  }
}

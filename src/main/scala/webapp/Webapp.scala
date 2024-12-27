package de.unruh.homeautomation
package webapp

import org.scalatra.{Route, ScalatraServlet, UrlGeneratorSupport}
import MyDevices.*

import controller.Controller

import scala.xml.NodeBuffer

class Webapp extends ScalatraServlet, UrlGeneratorSupport {
  error {
    case e: Throwable =>
      e.printStackTrace()
      Utils.stackTrace(e)
  }

  private val bedroomLightToggle = get("/bedroom/light/toggle") {
    bedroomCeilingLight.toggle()
    <b>Toggled bedroom light</b>
  }

  private val bedroomBlindsPosition = get("/bedroom/blinds/position/:pos") {
    blindsBedroom.setPosition(params("pos").toInt / 100.0)
    <b>Put blinds to {params("pos")}%</b>
  }

  private val lightCozyroom = get("/cozyroom/light") {
    Controller.lightCozyroom()
  }

  private val darknessCozyroom = get("/cozyroom/darkness") {
    Controller.darknessCozyroom()
  }

  private val darknessBedroom = get("/bedroom/darkness") {
    Controller.darknessBedroom()
  }

  private val darknessLivingroom = get("/livingroom/darkness") {
    Controller.darknessLivingroom()
  }

  private val darkness = get("/darkness") {
    Controller.darkness()
  }

  private val sleep = get("/sleep") {
    Controller.sleep()
  }

  private val leave = get("/leave") {
    val problems = Controller.leave()
    if (problems.nonEmpty) {
      <span>
        <h1>Please fix:</h1>
        <ul>{problems.map(p => <li>{p}</li>)}</ul>
      </span>
    } else {
      <h1>Don't forget your key. :)</h1>
    }
  }

  get("/") {
    def item(route: Route, params: (String, String)*) = {
      val myUrl = url(route, params*)
      <li><a href={myUrl}>{myUrl.stripPrefix("/home/")}</a></li>
    }

    <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
      </head>
      <body>
        <ul>
          {item(leave)}
          {item(bedroomLightToggle)}
          {item(darknessBedroom)}
          {item(lightCozyroom)}
          {item(darknessCozyroom)}
          {item(darknessLivingroom)}
          {item(darkness)}
          {item(sleep)}
          {item(bedroomBlindsPosition, "pos" -> "100")}
          {item(bedroomBlindsPosition, "pos" -> "0")}
        </ul>
      </body>
    </html>
  }
}


package de.unruh.homeautomation
package webapp

import org.scalatra.{Route, ScalatraServlet, UrlGeneratorSupport}
import MyDevices.*

import de.unruh.homeautomation.controller.Controller

import java.io.{PrintWriter, StringWriter}
import scala.xml.NodeBuffer

class Webapp extends ScalatraServlet, UrlGeneratorSupport {
  error {
    case e: Throwable =>
      e.printStackTrace()
      val writer = StringWriter()
      e.printStackTrace(PrintWriter(writer))
      writer.toString
  }

  private val bedroomLightToggle = get("/bedroom/light/toggle") {
    bedroomCeilingLight.toggle()
    <b>Toggled bedroom light</b>
  }

  private val bedroomBlindsPosition = get("/bedroom/blinds/position/:pos") {
    blindsBedroom.setPosition(params("pos").toInt / 100.0)
    <b>Put blinds to {params("pos")}%</b>
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

  get("/") {
    def item(route: Route, params: (String, String)*) = {
      val myUrl = url(route, params*)
      <li><a href={myUrl}>{myUrl}</a></li>
    }

    <html>
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
      </head>
      <body>
        <ul>
          {item(bedroomLightToggle)}
          {item(darknessBedroom)}
          {item(darknessCozyroom)}
          {item(darknessLivingroom)}
          {item(darkness)}
          {item(bedroomBlindsPosition, "pos" -> "100")}
          {item(bedroomBlindsPosition, "pos" -> "0")}
        </ul>
      </body>
    </html>
  }
}


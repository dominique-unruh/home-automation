package de.unruh.homeautomation
package webapp

import org.scalatra.{Route, ScalatraServlet, UrlGeneratorSupport}
import MyDevices.*

import scala.xml.NodeBuffer

class Webapp extends ScalatraServlet, UrlGeneratorSupport {
  private val bedroomLightOn = get("/bedroom/light/on") {
    bedroomCeilingLight.setOn(true)
    <b>Switched on bedroom light</b>
  }

  private val bedroomBlindsPosition = get("/bedroom/blinds/position/:pos") {
    blindsBedroom.setPosition(params("pos").toInt / 100.0)
    <b>Put blinds to {params("pos")}%</b>
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
          <li>Test</li>
          {item(bedroomLightOn)}
          {item(bedroomBlindsPosition, "pos" -> "90")}
        </ul>
      </body>
    </html>
  }
}


package de.unruh.homeautomation

import devices.{IKEABulb, RodretRemote}

object MyDevices {
  given mqtt: Mqtt = Mqtt()
  val bedroomCeilingLight = IKEABulb("zigbee2mqtt/light_bedroom_ceiling")
  val remote1 = RodretRemote("zigbee2mqtt/rodret_remote_1")
}

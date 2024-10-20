package de.unruh.homeautomation

import devices.{ParasollDoorWindowSensor, RodretRemote, TradfriBulb, TretaktPlug}

object MyDevices {
  given mqtt: Mqtt = Mqtt()
  lazy val bedroomCeilingLight = TradfriBulb("zigbee2mqtt/light_bedroom_ceiling")
  lazy val remote1 = RodretRemote("zigbee2mqtt/rodret_remote_1")
  lazy val windowBedroom = ParasollDoorWindowSensor("zigbee2mqtt/window_bedroom")
  lazy val plug1 = TretaktPlug("zigbee2mqtt/tretakt_plug_1")
}

package de.unruh.homeautomation

import devices.{ParasollDoorWindowSensor, RodretRemote, TradfriBulb, TretaktPlug}

object MyDevices {
  given mqtt: Mqtt = Mqtt()
  lazy val bedroomCeilingLight = TradfriBulb("zigbee2mqtt/light_bedroom_ceiling")
  lazy val remote1 = RodretRemote("zigbee2mqtt/rodret_remote_1")
  lazy val windowBedroom = ParasollDoorWindowSensor("zigbee2mqtt/window_bedroom")
  lazy val varmblixt = TretaktPlug("zigbee2mqtt/tretakt_plug_1")
  lazy val unused1 = TradfriBulb("zigbee2mqtt/light_cozyroom_ceiling")
  lazy val livingroomCeilingLightRight = TradfriBulb("zigbee2mqtt/light_living_ceiling_right")
  lazy val livingroomCeilingLightLeft = TradfriBulb("zigbee2mqtt/light_living_ceiling_left")
  lazy val floodlightTop = TradfriBulb("zigbee2mqtt/floodlight_top")
  // TODO: This is a bulb without color mode
  lazy val floodlightSide = TradfriBulb("zigbee2mqtt/floodlight_side")
}

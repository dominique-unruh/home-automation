package de.unruh.homeautomation

import devices.{ParasollDoorWindowSensor, RodretRemote, TradfriBulb, TredansenBlind, TretaktPlug}

object MyDevices {
  given mqtt: Mqtt = Mqtt()
  val bedroomCeilingLight = TradfriBulb("zigbee2mqtt/light_bedroom_ceiling")
  val remote1 = RodretRemote("zigbee2mqtt/rodret_remote_1")
  val windowBedroom = ParasollDoorWindowSensor("zigbee2mqtt/window_bedroom")
  val varmblixt = TretaktPlug("zigbee2mqtt/tretakt_plug_1")
  val unused1 = TradfriBulb("zigbee2mqtt/light_cozyroom_ceiling")
  val livingroomCeilingLightRight = TradfriBulb("zigbee2mqtt/light_living_ceiling_right")
  val livingroomCeilingLightLeft = TradfriBulb("zigbee2mqtt/light_living_ceiling_left")
  val floodlightTop = TradfriBulb("zigbee2mqtt/floodlight_top")
  // TODO: This is a bulb without color mode
  val floodlightSide = TradfriBulb("zigbee2mqtt/floodlight_side")
  val blindsBedroom = TredansenBlind("zigbee2mqtt/blinds_bedroom", windowBedroom)
}

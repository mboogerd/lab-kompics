package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import se.sics.kompics.PortType

/**
  *
  */
class PingPongPort extends PortType {
  request(classOf[Ping])
  indication(classOf[Pong])
}

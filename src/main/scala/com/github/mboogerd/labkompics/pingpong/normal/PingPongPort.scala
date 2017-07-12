package com.github.mboogerd.labkompics.pingpong.normal

import se.sics.kompics.PortType

/**
  *
  */
class PingPongPort extends PortType {
  request(classOf[Ping])
  indication(classOf[Pong])
}

package com.github.mboogerd.labkompics.pingpong.timer

import se.sics.kompics.PortType

/**
  *
  */
class PingPongPort extends PortType {
  request(classOf[Ping])
  indication(classOf[Pong])
}

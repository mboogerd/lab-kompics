package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.{TAddress, THeader, TMessage}
import se.sics.kompics.network.Transport

/**
  *
  */
object Ping {
  def apply(header: THeader): Ping = new Ping(header.getSource, header.getDestination)
}
class Ping(src: TAddress, dst: TAddress) extends TMessage(src, dst, Transport.TCP)

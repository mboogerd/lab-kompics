package com.github.mboogerd.labkompics.pingpong.networking.distributed

import com.github.mboogerd.labkompics.pingpong.networking.distributed.model.{TAddress, THeader, TMessage}
import se.sics.kompics.network.Transport

/**
  *
  */
object Pong {
  def apply(header: THeader): Pong = new Pong(header.getSource, header.getDestination)
}
class Pong(src: TAddress, dst: TAddress) extends TMessage(src, dst, Transport.TCP)

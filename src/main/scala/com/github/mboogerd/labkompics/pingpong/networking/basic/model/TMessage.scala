package com.github.mboogerd.labkompics.pingpong.networking.basic.model

import se.sics.kompics.network.{Msg, Transport}

/**
  *
  */
case class TMessage(override val getSource: TAddress,
                    override val getDestination: TAddress,
                    override val getProtocol: Transport) extends Msg[TAddress, THeader] {

  override val getHeader: THeader = THeader(getSource, getDestination, getProtocol)
}

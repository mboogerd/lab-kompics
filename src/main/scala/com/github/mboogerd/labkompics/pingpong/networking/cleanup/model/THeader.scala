package com.github.mboogerd.labkompics.pingpong.networking.cleanup.model

import se.sics.kompics.network.{Header, Transport}

/**
  *
  */
case class THeader(override val getSource: TAddress,
                   override val getDestination: TAddress,
                   override val getProtocol: Transport) extends Header[TAddress]

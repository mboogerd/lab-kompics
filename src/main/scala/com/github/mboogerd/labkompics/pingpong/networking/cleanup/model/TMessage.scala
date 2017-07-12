package com.github.mboogerd.labkompics.pingpong.networking.cleanup.model

import se.sics.kompics.network.{Msg, Transport}
import se.sics.kompics.{KompicsEvent, PatternExtractor}

/**
  *
  */
case class TMessage(override val getSource: TAddress,
                    override val getDestination: TAddress,
                    override val getProtocol: Transport,
                    payload: KompicsEvent) extends Msg[TAddress, THeader] with PatternExtractor[Class[_], KompicsEvent] {

  override val getHeader: THeader = THeader(getSource, getDestination, getProtocol)

  override val extractValue: KompicsEvent = payload

  override val extractPattern: Class[_] = payload.getClass
}

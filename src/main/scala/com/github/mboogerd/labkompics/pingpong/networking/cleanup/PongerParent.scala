package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.TAddress
import se.sics.kompics.network.Network
import se.sics.kompics.network.netty.{NettyInit, NettyNetwork}
import se.sics.kompics.{Channel, Component, ComponentDefinition, Init ⇒ KompicsInit}

/**
  *
  */
object PongerParent {
  case class Init(self: TAddress) extends KompicsInit[PongerParent]
}
class PongerParent(init: PongerParent.Init) extends ComponentDefinition {

  val ponger: Component = create(classOf[Ponger], Ponger.Init(init.self))
  val network: Component = create(classOf[NettyNetwork], new NettyInit(init.self))

  connect(ponger.getNegative(classOf[Network]), network.getPositive(classOf[Network]), Channel.TWO_WAY)
}

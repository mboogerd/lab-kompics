package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.TAddress
import se.sics.kompics.network.Network
import se.sics.kompics.network.netty.{NettyInit, NettyNetwork}
import se.sics.kompics.timer.Timer
import se.sics.kompics.timer.java.JavaTimer
import se.sics.kompics.{Channel, Component, ComponentDefinition, Init ⇒ KompicsInit}

/**
  *
  */
object PingerParent {
  case class Init(self: TAddress, ponger: TAddress) extends KompicsInit[PingerParent]

}
class PingerParent(init: PingerParent.Init) extends ComponentDefinition {

  val timer: Component = create(classOf[JavaTimer], KompicsInit.NONE)
  val network: Component = create(classOf[NettyNetwork], new NettyInit(init.self))
  val pinger: Component = create(classOf[Pinger], Pinger.Init(init.self, init.ponger))

  connect(pinger.getNegative(classOf[Timer]), timer.getPositive(classOf[Timer]), Channel.TWO_WAY)
  connect(pinger.getNegative(classOf[Network]), network.getPositive(classOf[Network]), Channel.TWO_WAY)
}

package com.github.mboogerd.labkompics.pingpong.networking.basic

import com.github.mboogerd.labkompics.pingpong.networking.basic.Parent._
import com.github.mboogerd.labkompics.pingpong.networking.basic.model.TAddress
import se.sics.kompics.network.Network
import se.sics.kompics.network.netty.{NettyInit, NettyNetwork}
import se.sics.kompics.timer.Timer
import se.sics.kompics.timer.java.JavaTimer
import se.sics.kompics.{Component, ComponentDefinition, Init ⇒ KompicsInit}
/**
  *
  */
object Parent {
  case class Init(self: TAddress) extends KompicsInit[Parent]
}
class Parent(init: Init) extends ComponentDefinition {
  val pinger1: Component = create(classOf[Pinger], Pinger.Init(init.self))
  val pinger2: Component = create(classOf[Pinger], Pinger.Init(init.self))
  val ponger: Component = create(classOf[Ponger], Ponger.Init(init.self))
  val timer: Component = create(classOf[JavaTimer], KompicsInit.NONE)
  val network: Component = create(classOf[NettyNetwork], new NettyInit(init.self))

  connect(pinger1.getNegative(classOf[Timer]), timer.getPositive(classOf[Timer]))
  connect(pinger2.getNegative(classOf[Timer]), timer.getPositive(classOf[Timer]))

  connect(pinger1.getNegative(classOf[Network]), network.getPositive(classOf[Network]))
  connect(pinger2.getNegative(classOf[Network]), network.getPositive(classOf[Network]))
  connect(ponger.getNegative(classOf[Network]), network.getPositive(classOf[Network]))
}




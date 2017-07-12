package com.github.mboogerd.labkompics.pingpong.normal

import se.sics.kompics.{Component, ComponentDefinition, Init}

/**
  *
  */
class Parent extends ComponentDefinition {
  val pinger: Component = create(classOf[Pinger], Init.NONE)
  val ponger: Component = create(classOf[Ponger], Init.NONE)

  connect(
    pinger.getNegative(classOf[PingPongPort]),
    ponger.getPositive(classOf[PingPongPort]))
}

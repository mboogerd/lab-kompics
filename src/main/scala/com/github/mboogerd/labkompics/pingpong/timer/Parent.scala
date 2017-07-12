package com.github.mboogerd.labkompics.pingpong.timer

import se.sics.kompics.timer.Timer
import se.sics.kompics.timer.java.JavaTimer
import se.sics.kompics.{Component, ComponentDefinition, Init}
/**
  *
  */
class Parent extends ComponentDefinition {
  val pinger1: Component = create(classOf[Pinger], Init.NONE)
  val pinger2: Component = create(classOf[Pinger], Init.NONE)
  val ponger: Component = create(classOf[Ponger], Init.NONE)
  val timer: Component = create(classOf[JavaTimer], Init.NONE)

  connect(pinger1.getNegative(classOf[PingPongPort]), ponger.getPositive(classOf[PingPongPort]))
  connect(pinger1.getNegative(classOf[Timer]), timer.getPositive(classOf[Timer]))
  connect(pinger2.getNegative(classOf[PingPongPort]), ponger.getPositive(classOf[PingPongPort]))
  connect(pinger2.getNegative(classOf[Timer]), timer.getPositive(classOf[Timer]))
}




package com.github.mboogerd.labkompics.pingpong.normal

import org.log4s._
import se.sics.kompics.{ComponentDefinition, Handler, Negative}

/**
  *
  */
class Ponger extends ComponentDefinition {
  private val log = getLogger
  private val port: Negative[PingPongPort] = provides(classOf[PingPongPort])

  private var counter: Long = 0L

  val pingHandler = new Handler[Ping] {
    override def handle(e: Ping): Unit = {
      counter += 1
      log.info(s"Got Ping #$counter!")
      trigger(new Pong(), port)
    }
  }

  subscribe(pingHandler, port)
}

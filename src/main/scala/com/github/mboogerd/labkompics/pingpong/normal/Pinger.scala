package com.github.mboogerd.labkompics.pingpong.normal

import org.log4s._
import se.sics.kompics.{ComponentDefinition, Handler, Positive, Start}

/**
  *
  */
class Pinger extends ComponentDefinition {
  private val log = getLogger
  private val port: Positive[PingPongPort] = requires(classOf[PingPongPort])

  private var counter: Long = 0L

  val startHandler = new Handler[Start] {
    override def handle(e: Start): Unit = trigger(new Ping(), port)
  }

  val pongHandler = new Handler[Pong] {
    override def handle(e: Pong): Unit = {
      counter += 1
      log.info(s"Got Pong #$counter!")
      trigger(new Ping(), port)
    }
  }

  subscribe(startHandler, control)
  subscribe(pongHandler, port)
}

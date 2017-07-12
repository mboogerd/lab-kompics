package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.{TAddress, TMessage}
import org.log4s._
import se.sics.kompics.network.{Network, Transport}
import se.sics.kompics.{ClassMatchedHandler, ComponentDefinition, Positive}

/**
  *
  */
object Ponger {
  case class Init(self: TAddress) extends se.sics.kompics.Init[Ponger]
}
class Ponger(init: Ponger.Init) extends ComponentDefinition {

  private[this] val log = getLogger

  val net: Positive[Network] = requires(classOf[Network])
  val self: TAddress = init.self

  private var counter: Long = 0L

  val pingHandler = new ClassMatchedHandler[Ping, TMessage]() {

    override def handle(v: Ping, e: TMessage): Unit = {
      counter += 1
      log.info(s"Got Ping #$counter!")
      trigger(TMessage(init.self, e.getSource, Transport.TCP, Pong()), net)
    }
  }

  subscribe(pingHandler, net)
}

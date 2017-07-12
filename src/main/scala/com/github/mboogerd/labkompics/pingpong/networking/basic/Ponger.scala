package com.github.mboogerd.labkompics.pingpong.networking.basic

import com.github.mboogerd.labkompics.pingpong.networking.basic.model.TAddress
import org.log4s._
import se.sics.kompics.network.Network
import se.sics.kompics.{ComponentDefinition, Handler, Positive}

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

  val pingHandler = new Handler[Ping] {
    override def handle(e: Ping): Unit = {
      counter += 1
      log.info(s"Got Ping #$counter!")
      trigger(new Pong(self, e.getSource), net)
    }
  }

  subscribe(pingHandler, net)
}

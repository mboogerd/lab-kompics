package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import java.util.UUID

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.TAddress
import com.github.mboogerd.labkompics.pingpong.timer.Pinger.PingTimeout
import org.log4s._
import se.sics.kompics.network.Network
import se.sics.kompics.timer.{CancelPeriodicTimeout, SchedulePeriodicTimeout, Timeout, Timer}
import se.sics.kompics.{ComponentDefinition, Handler, Positive, Start}

/**
  *
  */
object Pinger {
  class PingTimeout(spt: SchedulePeriodicTimeout) extends Timeout(spt)
  case class Init(self: TAddress, ponger: TAddress) extends se.sics.kompics.Init[Pinger]
}

class Pinger(init: Pinger.Init) extends ComponentDefinition {

  private[this] val log = getLogger
  private val net: Positive[Network] = requires(classOf[Network])
  private val timer: Positive[Timer] = requires(classOf[Timer])
  private val self: TAddress = init.self

  private var timerId: UUID = UUID.randomUUID()
  private var counter: Long = 0L

  val startHandler = new Handler[Start] {
    override def handle(e: Start): Unit = {
      val spt: SchedulePeriodicTimeout = new SchedulePeriodicTimeout(0, 1000)
      val timeout: PingTimeout = new PingTimeout(spt)
      spt.setTimeoutEvent(timeout)
      trigger(spt, timer)
      timerId = timeout.getTimeoutId
    }
  }

  val pongHandler = new Handler[Pong] {
    override def handle(e: Pong): Unit = {
      counter += 1
      log.info(s"Got Pong #$counter!")
    }
  }

  val timeoutHandler = new Handler[PingTimeout]() {
    override def handle(e: PingTimeout): Unit = trigger(new Ping(self, init.ponger), net)
  }

  subscribe(startHandler, control)
  subscribe(pongHandler, net)
  subscribe(timeoutHandler, timer)

  override def tearDown(): Unit = trigger(new CancelPeriodicTimeout(timerId), timer)
}

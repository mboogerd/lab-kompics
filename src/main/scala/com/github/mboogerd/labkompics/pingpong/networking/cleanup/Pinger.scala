package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import java.util.UUID

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.{TAddress, TMessage}
import com.github.mboogerd.labkompics.pingpong.timer.Pinger.PingTimeout
import org.log4s._
import se.sics.kompics._
import se.sics.kompics.network.{Network, Transport}
import se.sics.kompics.timer.{CancelPeriodicTimeout, SchedulePeriodicTimeout, Timeout, Timer}

import scala.concurrent.duration.FiniteDuration

/**
  *
  */
object Pinger {
  class PingTimeout(spt: SchedulePeriodicTimeout) extends Timeout(spt)
  case class Init(self: TAddress, ponger: TAddress, timeout: FiniteDuration) extends se.sics.kompics.Init[Pinger]
}

class Pinger(init: Pinger.Init) extends ComponentDefinition {

  private[this] val log = getLogger
  private val net: Positive[Network] = requires(classOf[Network])
  private val timer: Positive[Timer] = requires(classOf[Timer])

  private var timerId: UUID = UUID.randomUUID()
  private var counter: Long = 0L

  val startHandler = new Handler[Start] {
    override def handle(e: Start): Unit = {
      val spt: SchedulePeriodicTimeout = new SchedulePeriodicTimeout(0, init.timeout.toMillis)
      val timeout: PingTimeout = new PingTimeout(spt)
      spt.setTimeoutEvent(timeout)
      trigger(spt, timer)
      timerId = timeout.getTimeoutId
    }
  }

  val pongHandler = new ClassMatchedHandler[Pong, TMessage] {
    override def handle(v: Pong, e: TMessage): Unit = {
      counter += 1
      log.info(s"Got Pong #$counter!")
    }
  }

  val timeoutHandler = new Handler[PingTimeout]() {
    override def handle(e: PingTimeout): Unit =
      trigger(TMessage(init.self, init.ponger, Transport.TCP, Ping()), net)
  }

  subscribe(startHandler, control)
  subscribe(pongHandler, net)
  subscribe(timeoutHandler, timer)

  override def tearDown(): Unit = trigger(new CancelPeriodicTimeout(timerId), timer)
}

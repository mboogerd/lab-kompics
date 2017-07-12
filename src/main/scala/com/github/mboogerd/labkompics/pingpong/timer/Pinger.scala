package com.github.mboogerd.labkompics.pingpong.timer

import java.util.UUID

import com.github.mboogerd.labkompics.pingpong.timer.Pinger.PingTimeout
import org.log4s._
import se.sics.kompics.timer.{CancelPeriodicTimeout, SchedulePeriodicTimeout, Timeout, Timer}
import se.sics.kompics.{ComponentDefinition, Handler, Positive, Start}

/**
  *
  */
object Pinger {
  class PingTimeout(spt: SchedulePeriodicTimeout) extends Timeout(spt)
}
class Pinger extends ComponentDefinition {
  private val log = getLogger
  private val port: Positive[PingPongPort] = requires(classOf[PingPongPort])
  private val timer: Positive[Timer] = requires(classOf[Timer])
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
      // We now rely on the timer to send a new ping
//      trigger(new Ping(), port)
    }
  }

  val timeoutHandler = new Handler[PingTimeout]() {
    override def handle(e: PingTimeout): Unit = trigger(new Ping(), port)
  }

  subscribe(startHandler, control)
  subscribe(pongHandler, port)
  subscribe(timeoutHandler, timer)

  override def tearDown(): Unit = trigger(new CancelPeriodicTimeout(timerId), timer)
}

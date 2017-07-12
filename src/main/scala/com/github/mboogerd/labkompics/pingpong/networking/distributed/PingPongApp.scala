package com.github.mboogerd.labkompics.pingpong.networking.distributed

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.distributed.model.{TAddress, THeader}
import com.github.mboogerd.labkompics.pingpong.networking.distributed.serialization.{NetSerializer, PingPongSerializer}
import org.log4s._
import se.sics.kompics.Kompics
import se.sics.kompics.network.netty.serialization.Serializers

import scala.util.Try
import scala.util.control.NonFatal
import scala.concurrent.duration._
/**
  *
  */
object PingPongApp extends App {

  private[this] val log = getLogger
  private final val duration = 10.seconds

  // register
  Serializers.register(new NetSerializer, "netS")
  Serializers.register(new PingPongSerializer, "ppS")

  // map
  Serializers.register(classOf[TAddress], "netS")
  Serializers.register(classOf[THeader], "netS")
  Serializers.register(classOf[Ping], "ppS")
  Serializers.register(classOf[Pong], "ppS")

  args match {
    case Array(hostArg, portArg) ⇒
      // Launch Ponger
      val self = TAddress(InetAddress.getByName(hostArg), portArg.toInt)
      Kompics.createAndStart(classOf[PongerParent], PongerParent.Init(self), 2)
      log.info(s"Started Ponger @ $self (running indefinitely)")

    case Array(myHost, myPort, pongerHost, pongerPort) ⇒
      // Launch Pinger
      val self = TAddress(InetAddress.getByName(myHost), myPort.toInt)
      val ponger = TAddress(InetAddress.getByName(pongerHost), pongerPort.toInt)
      Kompics.createAndStart(classOf[PingerParent], PingerParent.Init(self, ponger), 2)
      log.info(s"Started Pinger @ $self to $ponger (running $duration)")

      // Wait for some time
      Thread.sleep(duration.toMillis)

      // Then shutdown
      Kompics.shutdown()
      System.exit(0)

    case _ ⇒
      log.error("Invalid number of parameters")
      System.exit(1)
  }
}

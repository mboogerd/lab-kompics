package com.github.mboogerd.labkompics.pingpong.networking.basic

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.basic.model.{TAddress, THeader}
import com.github.mboogerd.labkompics.pingpong.networking.basic.serialization.{NetSerializer, PingPongSerializer}
import org.log4s._
import se.sics.kompics.Kompics
import se.sics.kompics.network.netty.serialization.Serializers

import scala.util.Try
import scala.util.control.NonFatal

/**
  *
  */
object PingPongApp extends App {

  private[this] val log = getLogger

  // register
  Serializers.register(new NetSerializer, "netS")
  Serializers.register(new PingPongSerializer, "ppS")

  // map
  Serializers.register(classOf[TAddress], "netS")
  Serializers.register(classOf[THeader], "netS")
  Serializers.register(classOf[Ping], "ppS")
  Serializers.register(classOf[Pong], "ppS")

  try {
    val ip: InetAddress = InetAddress.getLocalHost
    val port: Int = Try(args(0).toInt).getOrElse(34567)
    val self: TAddress = TAddress(ip, port)

    // Initialize Kompics
    Kompics.createAndStart(classOf[Parent], Parent.Init(self), 2)

    // Wait for some time
    Thread.sleep(1000)

    // Then shutdown
    Kompics.shutdown()
    System.exit(0)
  } catch {
    case NonFatal(t) ⇒
      log.error(t)("Failed to start server")
      System.exit(1)
  }
}

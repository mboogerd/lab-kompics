package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.conf.ConfigReaders
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization.SerializerSetup
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.TAddress
import org.log4s.getLogger
import se.sics.kompics.Kompics
import pureconfig._
/**
  *
  */
object PongerApp extends App with SerializerSetup with ConfigReaders {
  case class Config(host: InetAddress, port: Int)
  private[this] val log = getLogger

  val conf = loadConfig[Config]("ponger")

  conf match {
    case Left(failures) ⇒
      log.error(failures.toString)
      System.exit(1)
    case Right(c) ⇒
      // Launch Ponger
      val self = TAddress(c.host, c.port)
      Kompics.createAndStart(classOf[PongerParent], PongerParent.Init(self), 2)
      log.info(s"Started Ponger @ $self (running indefinitely)")
  }
}

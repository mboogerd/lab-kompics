package com.github.mboogerd.labkompics.pingpong.networking.cleanup

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.conf.ConfigReaders
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.TAddress
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization.SerializerSetup
import org.log4s.getLogger
import pureconfig.loadConfig
import se.sics.kompics.Kompics

import scala.concurrent.duration.FiniteDuration

/**
  *
  */
object PingerApp extends App with SerializerSetup with ConfigReaders {

  case class Config(host: InetAddress, port: Int, timeout: FiniteDuration)

  private[this] val log = getLogger


  def startPinger(pingerConf: Config, pongerConf: PongerApp.Config): Unit = {

    // Launch Pinger
    val self = TAddress(pingerConf.host, pingerConf.port)
    val ponger = TAddress(pongerConf.host, pongerConf.port)
    Kompics.createAndStart(classOf[PingerParent], PingerParent.Init(self, ponger, pingerConf.timeout), 2)
    log.info(s"Started Pinger @ $self to $ponger (running ${pingerConf.timeout})")

    // Wait for some time
    Thread.sleep(pingerConf.timeout.toMillis)

    // Then shutdown
    Kompics.shutdown()
    System.exit(0)
  }

  val run = for {
    pingerConf ← loadConfig[Config]("pinger")
    pongerConf <- loadConfig[PongerApp.Config]("ponger")
  } yield startPinger(pingerConf, pongerConf)

  if(run.isLeft) {
    log.error(run.left.get.toString)
    System.exit(1)
  }
}

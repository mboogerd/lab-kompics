package com.github.mboogerd.labkompics.pingpong.normal

import se.sics.kompics.Kompics

import scala.util.Try
/**
  *
  */
object PingPongApp extends App {

  Kompics.createAndStart(classOf[Parent])

  Try(Thread.sleep(1000)).getOrElse(System.exit(1))

  Kompics.shutdown()

  System.exit(0)

}

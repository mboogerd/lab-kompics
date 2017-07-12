package com.github.mboogerd.labkompics.pingpong.networking.cleanup.conf

import java.net.InetAddress

import pureconfig._

import scala.util.Try
/**
  *
  */
trait ConfigReaders {

  implicit val inetAddressReader: ConfigReader[InetAddress] =
    ConfigReader.fromNonEmptyStringTry(s ⇒ Try(InetAddress.getByName(s)))

}

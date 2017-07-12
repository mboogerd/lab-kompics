package com.github.mboogerd.labkompics.pingpong.networking.distributed.model

import java.net.{InetAddress, InetSocketAddress}

import se.sics.kompics.network.Address

/**
  *
  */
case class TAddress(addr: InetAddress, port: Int) extends Address {

  override val getIp: InetAddress = addr

  override val getPort: Int = port

  override def sameHostAs(address: Address): Boolean = asSocket.equals(address.asSocket)

  override val asSocket: InetSocketAddress = new InetSocketAddress(addr, port)
}

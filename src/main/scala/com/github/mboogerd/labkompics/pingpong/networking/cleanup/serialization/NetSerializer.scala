package com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.{TAddress, THeader, TMessage}
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import se.sics.kompics.KompicsEvent
import se.sics.kompics.network.Transport
import se.sics.kompics.network.netty.serialization.{Serializer, Serializers}

/**
  *
  */
object NetSerializer {
  private final val ADDR: Byte = 1
  private final val HEADER: Byte = 2
  private final val MSG: Byte = 3
}
class NetSerializer extends Serializer {
  override final val identifier: Int = 100

  override def fromBinary(buf: ByteBuf, optional: Optional[AnyRef]): AnyRef = {
    buf.readByte() match {
      case NetSerializer.ADDR ⇒ addressFromBinary(buf)
      case NetSerializer.HEADER ⇒ headerFromBinary(buf)
      case NetSerializer.MSG ⇒
        val header = headerFromBinary(buf)
        val msg = Serializers.fromBinary(buf, Optional.absent[AnyRef]()).asInstanceOf[KompicsEvent]
        TMessage(header.getSource, header.getDestination, header.getProtocol, msg)
      case _ ⇒ null
    }
  }

  override def toBinary(o: scala.Any, buf: ByteBuf): Unit = o match {
    case addr: TAddress ⇒
      buf.writeByte(NetSerializer.ADDR) // mark which type we are serialising (1 byte)
      addressToBinary(addr, buf) // 6 bytes
    // total 7 bytes

    case header: THeader ⇒
      buf.writeByte(NetSerializer.HEADER); // mark which type we are serialising (1 byte)
      headerToBinary(header, buf)
      // total 14 bytes

    case msg: TMessage ⇒
      buf.writeByte(NetSerializer.MSG) // 1 byte
      headerToBinary(msg.getHeader, buf) // 13 bytes
      Serializers.toBinary(msg.payload, buf) // let the framework figure it out
      // total 14 + |payload|
  }


  def headerToBinary(header: THeader, buf: ByteBuf): Unit = {
    addressToBinary(header.getSource, buf) // 6 bytes
    addressToBinary(header.getDestination, buf) // 6 bytes
    buf.writeByte(header.getProtocol.ordinal()) // 1 byte
    // total of 13 bytes
  }

  def headerFromBinary(buf: ByteBuf): THeader = {
    val src: TAddress = addressFromBinary(buf)
    val dst: TAddress = addressFromBinary(buf)
    val protoOrd: Int = buf.readByte()
    val proto: Transport = Transport.values()(protoOrd)
    THeader(src, dst, proto)
  }

  def addressToBinary(addr: TAddress, buf: ByteBuf): Unit = {
    buf.writeBytes(addr.getIp.getAddress()); // 4 bytes IP (let's hope it's IPv4^^)
    buf.writeShort(addr.getPort); // we only need 2 bytes here
  }

  def addressFromBinary(buf: ByteBuf): TAddress = {
    val ipBytes = Array.ofDim[Byte](4)
    buf.readBytes(ipBytes)
    val ip = InetAddress.getByAddress(ipBytes)
    val port = buf.readUnsignedShort()
    TAddress(ip, port)
  }
}

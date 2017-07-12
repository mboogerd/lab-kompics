package com.github.mboogerd.labkompics.pingpong.networking.distributed.serialization

import java.net.InetAddress

import com.github.mboogerd.labkompics.pingpong.networking.distributed.model.{TAddress, THeader}
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import se.sics.kompics.network.Transport
import se.sics.kompics.network.netty.serialization.Serializer

/**
  *
  */
object NetSerializer {
  private final val ADDR: Byte = 1
  private final val HEADER: Byte = 2
}
class NetSerializer extends Serializer {
  override final val identifier: Int = 100

  override def fromBinary(buf: ByteBuf, optional: Optional[AnyRef]): AnyRef = {
    buf.readByte() match {
      case NetSerializer.ADDR ⇒
        val ipBytes = Array.ofDim[Byte](4)
        buf.readBytes(ipBytes)
        val ip = InetAddress.getByAddress(ipBytes)
        val port = buf.readUnsignedShort()
        TAddress(ip, port)

      case NetSerializer.HEADER ⇒
        val src: TAddress = this.fromBinary(buf, Optional.absent()).asInstanceOf[TAddress]
        val dst: TAddress = this.fromBinary(buf, Optional.absent()).asInstanceOf[TAddress]
        val protoOrd: Int = buf.readByte()
        val proto: Transport = Transport.values()(protoOrd)
        THeader(src, dst, proto)

      case _ ⇒ null
    }
  }

  override def toBinary(o: scala.Any, buf: ByteBuf): Unit = o match {
    case addr: TAddress ⇒
      buf.writeByte(NetSerializer.ADDR) // mark which type we are serialising (1 byte)
      buf.writeBytes(addr.getIp.getAddress()); // 4 bytes IP (let's hope it's IPv4^^)
      buf.writeShort(addr.getPort); // we only need 2 bytes here
    // total 7 bytes

    case header: THeader ⇒
      buf.writeByte(NetSerializer.HEADER); // mark which type we are serialising (1 byte)
      this.toBinary(header.getSource, buf); // use this serialiser again (7 bytes)
      this.toBinary(header.getDestination, buf); // use this serialiser again (7 bytes)
      buf.writeByte(header.getProtocol.ordinal()); // 1 byte is enough
      // total 16 bytes

    case _ ⇒
  }
}

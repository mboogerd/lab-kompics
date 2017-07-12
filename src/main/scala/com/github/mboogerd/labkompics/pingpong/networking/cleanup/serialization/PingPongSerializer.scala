package com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.{Ping, Pong}
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import se.sics.kompics.network.netty.serialization.Serializer

/**
  *
  */
object PingPongSerializer {
  protected[serialization] final val PING = 1
  protected[serialization] final val PONG = 2
}
class PingPongSerializer extends Serializer {

  override final val identifier: Int = 200

  override def fromBinary(buf: ByteBuf, optional: Optional[AnyRef]): AnyRef = {
    buf.readByte match {
      case PingPongSerializer.PING ⇒ Ping()
      case PingPongSerializer.PONG ⇒ Pong()
      case _ ⇒ null
    }
  }

  override def toBinary(o: scala.Any, buf: ByteBuf): Unit = o match {
    case Ping() ⇒ buf.writeByte(PingPongSerializer.PING)
    case Pong() ⇒ buf.writeByte(PingPongSerializer.PONG)
    case _ ⇒ // Don't crash for other object types
  }

}

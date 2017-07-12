package com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.{Ping, Pong}
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.THeader
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import se.sics.kompics.network.netty.serialization.{Serializer, Serializers}

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
    val msgType = buf.readByte
    msgType match {
      case PingPongSerializer.PING ⇒
        val header = Serializers.fromBinary(buf, Optional.absent[AnyRef])
        Ping(header.asInstanceOf[THeader])
      case PingPongSerializer.PONG ⇒
        val header = Serializers.fromBinary(buf, Optional.absent[AnyRef])
        Pong(header.asInstanceOf[THeader])
      case _ ⇒ null
    }
  }

  override def toBinary(o: scala.Any, buf: ByteBuf): Unit = o match {
    case p: Ping ⇒
      buf.writeByte(PingPongSerializer.PING)
      Serializers.toBinary(p.getHeader, buf)
    case p: Pong ⇒
      buf.writeByte(PingPongSerializer.PONG)
      Serializers.toBinary(p.getHeader, buf)
    case _ ⇒ // Don't crash for other object types
  }

}

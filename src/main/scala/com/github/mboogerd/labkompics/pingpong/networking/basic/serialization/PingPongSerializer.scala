package com.github.mboogerd.labkompics.pingpong.networking.basic.serialization

import com.github.mboogerd.labkompics.pingpong.networking.basic.{Ping, Pong}
import com.github.mboogerd.labkompics.pingpong.networking.basic.model.THeader
import com.google.common.base.Optional
import io.netty.buffer.ByteBuf
import org.log4s._
import se.sics.kompics.network.netty.serialization.{Serializer, Serializers}
/**
  *
  */
object PingPongSerializer {
  protected[serialization] final val PING: Byte = 1
  protected[serialization] final val PONG: Byte = 2
}
class PingPongSerializer extends Serializer {

  private[this] val log = getLogger
  override final val identifier: Int = 200

  override def fromBinary(buf: ByteBuf, optional: Optional[AnyRef]): AnyRef = {
    log.info(s"Received buffer with readable bytes: ${buf.readableBytes()}")
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

  override def toBinary(o: scala.Any, buf: ByteBuf): Unit = {
    log.info(s"Received object $o to serialize")
    o match {
      case p: Ping ⇒
        log.info("Writing Ping")
        buf.writeByte(PingPongSerializer.PING)
        Serializers.toBinary(p.getHeader, buf)
      case p: Pong ⇒
        log.info("Writing Pong")
        buf.writeByte(PingPongSerializer.PONG)
        Serializers.toBinary(p.getHeader, buf)
      case _ ⇒ // Don't crash for other object types
        log.info("Writing Nothing")
    }
  }

}

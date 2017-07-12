package com.github.mboogerd.labkompics.pingpong.networking.cleanup.serialization

import com.github.mboogerd.labkompics.pingpong.networking.cleanup.model.{TAddress, THeader, TMessage}
import com.github.mboogerd.labkompics.pingpong.networking.cleanup.{Ping, Pong}
import se.sics.kompics.network.netty.serialization.Serializers

/**
  *
  */
trait SerializerSetup {

  // register
  Serializers.register(new NetSerializer, "netS")
  Serializers.register(new PingPongSerializer, "ppS")

  // map
  Serializers.register(classOf[TAddress], "netS")
  Serializers.register(classOf[THeader], "netS")
  Serializers.register(classOf[TMessage], "netS")
  Serializers.register(classOf[Ping], "ppS")
  Serializers.register(classOf[Pong], "ppS")
}

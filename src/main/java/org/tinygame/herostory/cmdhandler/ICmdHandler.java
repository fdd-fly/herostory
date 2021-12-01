package org.tinygame.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface ICmdHandler<TCmd extends GeneratedMessageV3> {
    void handle(ChannelHandlerContext ctx,TCmd cmd);
}

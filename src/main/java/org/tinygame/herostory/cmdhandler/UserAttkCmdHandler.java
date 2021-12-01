package org.tinygame.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zqs
 * @date 2021-11-23 21:56
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {
    static private final Logger log= LoggerFactory.getLogger(UserAttkCmdHandler.class);
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd userAttkCmd) {
        log.info("UserAttk");
    }
}

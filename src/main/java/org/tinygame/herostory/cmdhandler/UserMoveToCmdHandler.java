package org.tinygame.herostory.cmdhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zqs
 * @date 2021-11-23 22:09
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        if (null==ctx||null==cmd){
            return;
        }
        Integer userId= (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null==userId){
            return;
        }
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder=GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());

        GameMsgProtocol.UserMoveToResult newResult=resultBuilder.build();
        Broadcaster.broadcast(newResult);

    }
}

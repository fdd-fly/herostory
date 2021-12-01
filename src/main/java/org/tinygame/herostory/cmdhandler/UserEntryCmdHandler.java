package org.tinygame.herostory.cmdhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.User;
import org.tinygame.herostory.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zqs
 * @date 2021-11-23 21:59
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd>{
   static private final Logger log= LoggerFactory.getLogger(UserEntryCmdHandler.class);
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd cmd) {
        if (null==ctx||null==cmd){
            return;
        }
        int userId=cmd.getUserId();
        String heroAvatar=cmd.getHeroAvatar();
        User newUser=new User();
        newUser.userId=userId;
        newUser.heroAvatar=heroAvatar;
        UserManager.addUser(newUser);

        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        GameMsgProtocol.UserEntryResult.Builder resultBuiler=GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuiler.setUserId(userId);
        resultBuiler.setHeroAvatar(heroAvatar);

        GameMsgProtocol.UserEntryResult newResult=resultBuiler.build();
        Broadcaster.broadcast(newResult);

    }
}

















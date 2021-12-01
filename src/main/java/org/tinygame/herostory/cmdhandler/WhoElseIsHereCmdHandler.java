package org.tinygame.herostory.cmdhandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.User;
import org.tinygame.herostory.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Collection;

/**
 * @author zqs
 * @date 2021-11-23 22:15
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd cmd) {
        if (null==ctx||null==cmd){
            return;
        }
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder=GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        Collection<User> userList= UserManager.listUser();
        for (User currUser:userList){
            if (null==currUser){
                continue;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder=
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currUser.userId);
            userInfoBuilder.setHeroAvatar(currUser.heroAvatar);
            resultBuilder.addUserInfo(userInfoBuilder);
        }
        GameMsgProtocol.WhoElseIsHereResult newResult=resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}

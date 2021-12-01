package org.tinygame.herostory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zqs
 * @date 2021-11-17 21:04
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    static private Logger log= LoggerFactory.getLogger(GameMsgHandler.class);
    static private final ChannelGroup _channelGroup =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static private final Map<Integer,User> _userMap=new HashMap<>();
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        if (null==ctx){
            return;
        }
        try {
            super.channelActive(ctx);
            _channelGroup.add(ctx.channel());
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null==ctx|| null==msg){
            return;
        }
        log.info("收到客户端消息，msgClazz = {},msgBody={}",msg.getClass().getSimpleName(),msg);
        try {
            if (msg instanceof GameMsgProtocol.UserEntryCmd){
                GameMsgProtocol.UserEntryCmd cmd= (GameMsgProtocol.UserEntryCmd) msg;
                int userId=cmd.getUserId();
                String heroAvatar=cmd.getHeroAvatar();

                User newUser=new User();
                newUser.userId=userId;
                newUser.heroAvatar=heroAvatar;
                _userMap.putIfAbsent(userId,newUser);

                GameMsgProtocol.UserEntryResult.Builder resultBuilder=GameMsgProtocol.UserEntryResult.newBuilder();
                resultBuilder.setUserId(userId);
                resultBuilder.setHeroAvatar(heroAvatar);

                GameMsgProtocol.UserEntryResult newResult=resultBuilder.build();
                _channelGroup.writeAndFlush(newResult);
            }else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd){
                GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder=GameMsgProtocol.WhoElseIsHereResult.newBuilder();
                for (User currUser:_userMap.values()){
                    if (null==currUser){
                        continue;
                    }
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder=GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();

                    userInfoBuilder.setUserId(currUser.userId);
                    userInfoBuilder.setHeroAvatar(currUser.heroAvatar);
                    resultBuilder.addUserInfo(userInfoBuilder);
                }
                GameMsgProtocol.WhoElseIsHereResult newResult=resultBuilder.build();
                ctx.writeAndFlush(newResult);
            }else if (msg instanceof GameMsgProtocol.UserMoveToCmd){
                Integer userId=(Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
                if (null==userId){
                    return;
                }
                GameMsgProtocol.UserMoveToCmd cmd= (GameMsgProtocol.UserMoveToCmd) msg;
                GameMsgProtocol.UserMoveToResult.Builder resultBuilder =GameMsgProtocol.UserMoveToResult.newBuilder();
                resultBuilder.setMoveUserId(userId);
                resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
                resultBuilder.setMoveToPosY(cmd.getMoveToPosY());

                GameMsgProtocol.UserMoveToResult newResult=resultBuilder.build();
                _channelGroup.writeAndFlush(newResult);

            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}














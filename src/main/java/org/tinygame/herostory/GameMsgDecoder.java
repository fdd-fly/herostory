package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zqs
 * @date 2021-11-18 22:14
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    static private final Logger log= LoggerFactory.getLogger(GameMsgDecoder.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        if (null==ctx||null==msg){
            return;
        }
        if (!(msg instanceof BinaryWebSocketFrame)){
            return;
        }
        try {
            BinaryWebSocketFrame inputFrame= (BinaryWebSocketFrame) msg;
            ByteBuf byteBuf=inputFrame.content();

            byteBuf.readShort();
            int msgCode=byteBuf.readShort();

            byte[] msgBody=new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(msgBody);

            GeneratedMessageV3 cmd=null;
            switch (msgCode){
                case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                    cmd=GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                    break;
                case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                    cmd=GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                    break;


                case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                    cmd=GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                    break;
                default:break;
            }
            if (null!=cmd){
                ctx.fireChannelRead(cmd);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}

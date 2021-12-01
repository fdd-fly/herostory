package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author zqs
 * @date 2021-11-18 22:36
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
    static private final Logger log= LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise){
        if (null==ctx||null==msg){
            return;
        }

        try {
            if (!(msg instanceof GeneratedMessageV3)){
                super.write(ctx,msg,promise);
                return;
            }
            int msgCode=-1;
            if (msg instanceof GameMsgProtocol.UserEntryResult){
                msgCode=GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
            }else if (msg instanceof GameMsgProtocol.WhoElseIsHereResult){
                msgCode=GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
            } else if (msg instanceof GameMsgProtocol.UserMoveToResult){
                msgCode=GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
            } else {
                log.error("无法识别消息类型，msgClazz={}",msg.getClass().getSimpleName());
                super.write(ctx,msg,promise);
                return;
            }

            byte[] msgBody=((GeneratedMessageV3)msg).toByteArray();
            ByteBuf byteBuf=ctx.alloc().buffer();
            byteBuf.writeShort((short)msgBody.length);
            byteBuf.writeShort((short)msgCode);
            byteBuf.writeBytes(msgBody);

            BinaryWebSocketFrame outputFrame=new BinaryWebSocketFrame(byteBuf);
            super.write(ctx,outputFrame,promise);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
}

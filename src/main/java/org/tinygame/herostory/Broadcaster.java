package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 广播员
 * @author zqs
 * @date 2021-11-22 22:08
 */
public final class Broadcaster {
    /**
     * 信道组, 注意这里一定要用 static,
     * 否则无法实现群发
     */
    static private final ChannelGroup _channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){
    }

    static public void addChannel(Channel ch){
        if (null==ch){
            _channelGroup.add(ch);
        }
    }

    static private void remove(Channel ch){
        if (null==ch){
            _channelGroup.remove(ch);
        }
    }

    static public void broadcast(Object msg){
        if (null!=msg){
            _channelGroup.writeAndFlush(msg);
        }
    }
}

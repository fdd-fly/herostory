package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdhandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdhandler.ICmdHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zqs
 * @date 2021-12-02 22:09
 */
public final class MainThreadProcessor {
    static private final Logger log= LoggerFactory.getLogger(MainThreadProcessor.class);

    static private final MainThreadProcessor _instance=new MainThreadProcessor();

    private final ExecutorService _es= Executors.newSingleThreadExecutor((r)->{
        Thread newThread=new Thread(r);
        newThread.setName("MainMsgProcessor");
        return newThread;
    });

    private MainThreadProcessor(){
    }

    static public MainThreadProcessor getInstance(){
        return _instance;
    }

    private void process(ChannelHandlerContext ctx, Object msg){
        if (null==ctx||null==msg){
            return;
        }

        final Class<?> msgClazz=msg.getClass();

        log.info("收到客户端消息，msgclazz={}",msgClazz.getName());

        final ICmdHandler<? extends GeneratedMessageV3> handlerImpl= CmdHandlerFactory.create(msgClazz);
        if (null==handlerImpl){
            log.error("未找到想对应的命令处理器，msgClazz={}",msgClazz.getName());
            return;
        }
        this.process(()->handlerImpl.handle(ctx,cast(msg)));
    }

    public void process(Runnable r){
        if (null!=r){
            _es.submit(new SafeRun(r));
        }
    }

    @SuppressWarnings("unchecked")
    static private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if (!(msg instanceof GeneratedMessageV3)){
            return null;
        }else {
            return (TCmd) msg;
        }
    }

    static private class SafeRun implements Runnable{

        private final Runnable _innerR;

        SafeRun(Runnable innerR){
            _innerR=innerR;
        }

        @Override
        public void run() {
            if (null==_innerR){
                return;
            }
            try {
                _innerR.run();
            }catch (Exception ex){
                log.error(ex.getMessage(),ex);
            }
        }
    }
}

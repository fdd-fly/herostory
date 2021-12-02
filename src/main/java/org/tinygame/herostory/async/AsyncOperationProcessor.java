package org.tinygame.herostory.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MainThreadProcessor;

import java.text.MessageFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zqs
 * @date 2021-12-02 21:47
 */
public final class AsyncOperationProcessor {
    static private final Logger log= LoggerFactory.getLogger(AsyncOperationProcessor.class);

    static private final AsyncOperationProcessor _instance=new AsyncOperationProcessor();

    private final ExecutorService[] _esArray=new ExecutorService[10];

    private AsyncOperationProcessor(){
        for (int i=0;i< _esArray.length;i++){
            final String threadName= MessageFormat.format("AsyncOperationProcessor[{0}]",i);
            _esArray[i]= Executors.newSingleThreadExecutor((r) -> {
                Thread t=new Thread(r);
                t.setName(threadName);
                return t;
            });
        }
    }

    static public AsyncOperationProcessor getInstance(){
        return _instance;
    }

    public void process(IAsyncOperation op){
        if (null==op){
            return;
        }

        int bindId=Math.abs(op.getBindId());
        int esIndex=bindId % _esArray.length;

        _esArray[esIndex].submit(()->{
            try {
                //执行异步操作
                op.doAsync();
                //回到主线程执行完成逻辑
                MainThreadProcessor.getInstance().process(op::doFinish);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        });
    }


}

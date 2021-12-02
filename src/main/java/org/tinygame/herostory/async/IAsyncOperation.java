package org.tinygame.herostory.async;

/**
 * 异步接口
 */
public interface IAsyncOperation {
    /**
     * 获取绑定id
     * @return
     */
    default int getBindId(){
        return 0;
    }

    /**
     * 执行异步操作
     */
    void doAsync();

    /**
     * 执行逻辑
     */
    default void doFinish(){
    }
}

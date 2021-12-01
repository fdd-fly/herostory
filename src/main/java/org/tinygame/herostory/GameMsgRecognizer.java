package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息识别器
 * @author zqs
 * @date 2021-11-22 21:36
 */
public final class GameMsgRecognizer {
    static final Logger log= LoggerFactory.getLogger(GameMsgRecognizer.class);

    static private final Map<Class<?>,Integer> _msgClazzAndMsgCodeMap=new HashMap<>();

    static private final Map<Integer,GeneratedMessageV3> _msgCodeAndMsgObjMap=new HashMap<>();
    private GameMsgRecognizer(){
    }

    static public void init(){
        log.info("====");

        Class<?>[] innerClazzArray= GameMsgProtocol.class.getDeclaredClasses();
        for (Class<?> innerClass: innerClazzArray) {
            if (null==innerClass||!GeneratedMessageV3.class.isAssignableFrom(innerClass)){
                continue;
            }

            String clazzName=innerClass.getSimpleName();
            clazzName=clazzName.toLowerCase();

            for (GameMsgProtocol.MsgCode msgCode:GameMsgProtocol.MsgCode.values()){
                if (null==msgCode){
                    continue;
                }
                String strMsgCode=msgCode.name();
                strMsgCode=strMsgCode.replaceAll("-","");
                strMsgCode=strMsgCode.toLowerCase();
                if (!strMsgCode.startsWith(clazzName)){
                    continue;
                }

                try {
                    Object returnObj=innerClass.getDeclaredMethod("getDefaultInstance").invoke(innerClass);
                    log.info("{}-->{}",innerClass.getName(),msgCode.getNumber());
                    _msgCodeAndMsgObjMap.put(msgCode.getNumber(), (GeneratedMessageV3) returnObj);
                    _msgClazzAndMsgCodeMap.put(innerClass,msgCode.getNumber());

                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }

    }

    static public Message.Builder getBuilderByMsgCode(int msgCode){
        if (msgCode<0){
            return null;
        }
        GeneratedMessageV3 defaultMsg=_msgCodeAndMsgObjMap.get(msgCode);
        if (null==defaultMsg){
            return null;
        }else {
            return defaultMsg.newBuilderForType();
        }
    }

    static public int getMsgCodeClass(Class<?> msgClazz){
        if (null==msgClazz){
            return -1;
        }
        Integer msgCode=_msgClazzAndMsgCodeMap.get(msgClazz);
        if (null==msgCode){
            return -1;
        }else {
            return msgCode.intValue();
        }
    }
}
























































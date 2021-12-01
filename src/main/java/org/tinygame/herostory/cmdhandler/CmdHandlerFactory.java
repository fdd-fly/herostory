package org.tinygame.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zqs
 * @date 2021-11-23 21:30
 */
public class CmdHandlerFactory {
    static private final Logger log= LoggerFactory.getLogger(CmdHandlerFactory.class);

    static private final Map<Class<?>,ICmdHandler<? extends GeneratedMessageV3>> _handlerMap=new HashMap<>();

    private CmdHandlerFactory(){

    }


    static public ICmdHandler<? extends GeneratedMessageV3> create(Class<?>msgClazz){
        if (null==msgClazz){
            return null;
        }
        return _handlerMap.get(msgClazz);
    }

    static public void init(){
        log.info("===完成与处理器关联的命令===");
        final String packageName=CmdHandlerFactory.class.getPackage().getName();

        Set<Class<?>> clazzSet= PackageUtil.listSubClazz(
                packageName,true,ICmdHandler.class);
        for (Class<?> handlerClazz:clazzSet){
            if (null==handlerClazz||
            0!=(handlerClazz.getModifiers()& Modifier.ABSTRACT)){
                continue;
            }
            Method[] methodArray=handlerClazz.getDeclaredMethods();
            Class<?> cmdClazz=null;
            for (Method currMethod:methodArray){
                if (null==currMethod||!currMethod.getName().equals("handle")){
                    continue;
                }
                Class<?>[] paramTypeArray=currMethod.getParameterTypes();
                if (paramTypeArray.length<2||
                paramTypeArray[1]==GeneratedMessageV3.class||
                !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])){
                    continue;
                }
                cmdClazz=paramTypeArray[1];
                break;
            }
            if (null==cmdClazz){
                continue;
            }
            try {
                ICmdHandler<?> newHandler= (ICmdHandler<?>) handlerClazz.newInstance();
                log.info("{}--->{}",cmdClazz.getName(),handlerClazz.getName());
                _handlerMap.put(cmdClazz,newHandler);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
    }

}













































package org.tinygame.herostory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户管理器
 * @author zqs
 * @date 2021-11-22 21:28
 */
public final class UserManager {
    static private final Map<Integer,User> _userMap=new ConcurrentHashMap<>();

    private UserManager(){}

    static public void addUser(User u){
        if (null!=u){
            _userMap.putIfAbsent(u.userId, u);
        }
    }

    static public void removeByUserId(int userId){
        _userMap.remove(userId);
    }

    static public Collection<User> listUser(){
        return _userMap.values();
    }
}

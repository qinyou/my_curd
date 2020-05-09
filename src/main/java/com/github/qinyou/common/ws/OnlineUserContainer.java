package com.github.qinyou.common.ws;


import com.github.qinyou.system.model.SysUser;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户
 * WebSocket session 和 用户系统关联 (可能WebSocket 规范中有更好的方法)
 *
 * @author chuang
 * @date 2018/9/10
 */
public class OnlineUserContainer {

    // ws sessionId -> ws session
    public final static Map<String, Session> SESSIONID_SESSION = new ConcurrentHashMap<>();
    // ws sessionId -> SysUser
    public final static Map<String, SysUser> SESSIONID_USER = new ConcurrentHashMap<>();
    // SysUser userid -> ws sessionId
    public final static Map<String, String> USERID_SESSIONID = new ConcurrentHashMap<>();


    /**
     * 添加在线用户，ws 建立连接时使用
     *
     * @param authUser
     * @param session
     */
    public static void addOnlineUser(SysUser authUser, Session session) {
        SESSIONID_USER.put(session.getId(), authUser);
        SESSIONID_SESSION.put(session.getId(), session);
        USERID_SESSIONID.put(authUser.getId(), session.getId());
    }

    /**
     * 移除在线用户，ws 关闭 或发生异常时使用
     *
     * @param authUser
     * @param session
     */
    public static void removeOnlineUser(SysUser authUser, Session session) {
        OnlineUserContainer.SESSIONID_SESSION.remove(session.getId());
        OnlineUserContainer.SESSIONID_USER.remove(session.getId());
        OnlineUserContainer.USERID_SESSIONID.remove(authUser.getId());
    }


    /**
     * 获得在线用户信息（即时通讯使用)
     *
     * @return
     */
    public static List<SysUser> getOnlineUserInfo() {
        List<SysUser> authUsers = new ArrayList<>();
        for (Map.Entry<String, SysUser> entry : SESSIONID_USER.entrySet()) {
            // userid 加密
            entry.getValue().set("userid", UserIdEncryptUtils.encrypt(entry.getValue().getId(), UserIdEncryptUtils.OTHER_USERS_ID_AESKEY));
            entry.getValue().setId(null);
            entry.getValue().setPassword(null);
            authUsers.add(entry.getValue());
        }
        return authUsers;
    }
}

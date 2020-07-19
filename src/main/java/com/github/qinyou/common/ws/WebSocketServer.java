package com.github.qinyou.common.ws;

import com.github.qinyou.system.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


/**
 * websocket server
 */
@ServerEndpoint("/ws-server")
@Slf4j
public class WebSocketServer {

    // 无操作最大超时时间  1800 秒
    private final static Integer MAX_IDLE_TIMEOUT = 0;

    /**
     * 关闭当前会话
     *
     * @param session
     */
    public static void closeSession(Session session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 建立 ws 连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(MAX_IDLE_TIMEOUT);

        // 输出默认信息
        log.debug("新建 ws连接 sessionId:  {}", session.getId());
        log.debug("maxIdleTimeout: {}", session.getMaxIdleTimeout());

        // url参数
        String queryString = session.getQueryString();
        String userId = queryString.split("=")[1];

        // aes 解密
        try {
            userId = UserIdEncryptUtils.decrypt(userId, UserIdEncryptUtils.CURRENT_USER_ID_AESKEY);
        } catch (Exception e) {
            closeSession(session);
            log.error(e.getMessage(), e);
            return;
        }
        SysUser sysUser = SysUser.dao.findById(userId);
        if (sysUser == null) {
            closeSession(session);
            return;
        }

        // SendMsgUtils.broadcast(authUser.getName() + " online now ...", session.getId());
        // 系统 用户可以重复登录，但不可 重复连接 WebSocket (新的 webSocket 连接直接被关闭)
        if (OnlineUserContainer.USERID_SESSIONID.containsKey(userId)) {
            if (log.isInfoEnabled()) {
                log.debug("userId:{} 不同浏览器 重复连接 WebSocket, 直接关闭...... ", userId);
            }
            closeSession(session);
            return;
        }

        // 用户信息存入 map
        OnlineUserContainer.addOnlineUser(sysUser, session);
    }

    /**
     * 关闭 ws 连接
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        if (log.isInfoEnabled()) {
            log.debug(" 关闭  ws sessionId: " + session.getId());
        }
        String sessionId = session.getId();
        SysUser sysUser = OnlineUserContainer.SESSIONID_USER.get(sessionId);

        // 一个账号重复登录，在 onOpen方法中会 执行 closeSession，此时 sysUser 为 null, 不应该将用户信息移除;
        // 其它 情况 此 authUser 不应该为 null
        if (sysUser != null) {
            // 用户信息 移出 map
            OnlineUserContainer.removeOnlineUser(sysUser, session);
        }
    }

    /**
     * 处理文本消息
     * https://docs.oracle.com/javaee/7/api/javax/websocket/OnMessage.html
     *
     * @param msg     json 格式的数据
     * @param session
     */
    @OnMessage // 一个 endpoint 只能有一个该方法注解
    public void onMessage(String msg, Session session) {
        SysUser sysUser = OnlineUserContainer.SESSIONID_USER.get(session.getId());
        if (log.isInfoEnabled()) {
            log.debug(" 收到消息：" + msg);
        }
    }

    /**
     * 发生异常 触发
     *
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
        log.error(t.getMessage(), t);
        closeSession(session);
    }
}

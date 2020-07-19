package com.github.qinyou.common.ws;

import com.github.qinyou.common.utils.thread.ExecutorServiceUtils;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 发送 websocket 消息
 *
 * @author chuang
 */
@Slf4j
public class SendMsgUtils {

    /**
     * 送 消息给部分用户发
     *
     * @param userIds sys_user id
     * @param msg     消息文本
     */
    public static void sendToUsers(Set<String> userIds, String msg) {
        for (String userId : userIds) {
            ExecutorServiceUtils.pool.submit(() -> {
                String sessionId = OnlineUserContainer.USERID_SESSIONID.get(userId);
                if (StrKit.isBlank(sessionId)) {
                    log.debug("用户：{} websocket 不在线，不使用 WebSocket 推送。", userId);
                    return;
                }
                Session session = OnlineUserContainer.SESSIONID_SESSION.get(sessionId);
                if (session == null) {
                    log.debug("用户：{} 找不到 websocket session，不使用 WebSocket 推送。", userId);
                    return;
                }
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    WebSocketServer.closeSession(session);
                    log.error(e.getMessage(), e);
                }
            });
        }
    }
    /**
     * 发送消息给部分用户
     *
     * @param sessionIds
     * @param msg
     */
    public static void sendToUsers(List<String> sessionIds, String msg) {
        Session session;
        for (String sessionId : sessionIds) {
            if (sessionId == null) {
                continue;
            }
            session = OnlineUserContainer.SESSIONID_SESSION.get(sessionId);
            if (session != null) {
                try {
                    session.getBasicRemote().sendText(msg);
                } catch (IOException e) {
                    WebSocketServer.closeSession(session);
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * 广播通知所有在线客户
     *
     * @param message   消息文本
     * @param sessionId 排除的当前session
     */
    public static void broadcast(String message, String sessionId) {
        log.debug("broadcast msg : {}", message);
        ExecutorServiceUtils.pool.submit(() -> {
            Session session;
            for (String mapKey : OnlineUserContainer.SESSIONID_SESSION.keySet()) {
                // 排除自己
                if (sessionId == null || mapKey.equals(sessionId)) {
                    continue;
                }
                session = OnlineUserContainer.SESSIONID_SESSION.get(mapKey);
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    WebSocketServer.closeSession(session);
                    log.error(e.getMessage(), e);
                }
            }
        });
    }
}

package com.github.qinyou.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 将 session 中部分属性 放入到 request 中
 *
 * @author chuang
 */
public class SessionInViewInterceptor implements Interceptor {

    // 需 放入 request 的 session 中属性字段 列表
    private List<String> sessionFields;

    public SessionInViewInterceptor(List<String> sessionFields) {
        this.sessionFields = sessionFields;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void intercept(Invocation inv) {
        inv.invoke();

        Controller c = inv.getController();
        if (c.getRender() instanceof com.jfinal.render.JsonRender) {
            return;
        }
        HttpSession hs = c.getSession(false);
        if (hs != null) {
            Map session = new com.jfinal.ext.interceptor.SessionInViewInterceptor.JFinalSession(hs);
            for (String sessionField : sessionFields) {
                session.put(sessionField, hs.getAttribute(sessionField));
            }
            c.setAttr("session", session);
        }
    }
}

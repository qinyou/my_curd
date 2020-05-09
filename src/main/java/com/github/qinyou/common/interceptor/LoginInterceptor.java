package com.github.qinyou.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 *
 * @author chuang
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        HttpServletRequest request = invocation.getController().getRequest();
        HttpSession session = request.getSession();
        // 未登录 跳转到登录页面
        if (session.getAttribute("sysUser") == null) {
            invocation.getController().redirect("/login");
            return;
        }
        invocation.invoke();
    }
}

package com.github.qinyou.common.web;

import com.alibaba.fastjson.JSON;
import com.github.qinyou.common.interceptor.LoginInterceptor;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.guava.BaseCache;
import com.github.qinyou.common.utils.guava.CacheContainer;
import com.github.qinyou.common.utils.guava.LoginRetryLimitCache;
import com.github.qinyou.system.model.SysMenu;
import com.github.qinyou.system.model.SysUser;
import com.github.qinyou.system.model.SysUserRole;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录 controller
 */
@Clear({LoginInterceptor.class, PermissionInterceptor.class})
@Slf4j
public class LoginController extends BaseController {
    // 登录用户名密码cookie key
    private final static String USERNAME_KEY = "mycurd_username";
    private final static String PASSWORD_KEY = "mycurd_password";

    /**
     * 登录页面
     */
    public void index() {
        String username = getCookie(USERNAME_KEY);
        String password = getCookie(PASSWORD_KEY);
        log.debug("username from cookie: {}", username);
        log.debug("password from cookie:{}", password);
        // cookie username password 存在
        if (StringUtils.notEmpty(username) && StringUtils.notEmpty(password)) {
            SysUser sysUser = SysUser.dao.findByUsernameAndPassword(username, password);
            if (sysUser != null && "0".equals(sysUser.getUserState())) {
                sysUser.setLastLoginTime(new Date());
                sysUser.update();

                afterLogin(sysUser);

                redirect("/dashboard");
                return;
            } else {
                // 清理 记住密码 cookie
                setCookie(USERNAME_KEY, null, 0);
                setCookie(PASSWORD_KEY, null, 0);
            }
        }
        render("login.ftl");
    }

    /**
     * 登录表单提交地址
     */
    public void action() {
        String username = getPara("username");
        String password = getPara("password");

        /* username password 无效 */
        if (StrKit.isBlank(username)) {
            setAttr("errMsg", "请填写用户名。");
            render("login.ftl");
            return;
        }
        if (StrKit.isBlank(password)) {
            setAttr("errMsg", "请填写密码。");
            render("login.ftl");
            return;
        }
        SysUser sysUser = SysUser.dao.findByUsername(username);
        if (sysUser == null) {
            setAttr("errMsg", username + " 用户不存在。");
            render("login.ftl");
            return;
        }

        // 密码错误 n 次 锁定 m 分钟
        BaseCache<String, AtomicInteger> retryCache = CacheContainer.getLoginRetryLimitCache();
        AtomicInteger retryTimes = retryCache.getCache(username);
        if (retryTimes.get() >= LoginRetryLimitCache.RETRY_LIMIT) {
            setAttr("username", username);
            setAttr("errMsg", " 账号已被锁定, " + LoginRetryLimitCache.LOCK_TIME + "分钟后可自动解锁。 ");
            render("login.ftl");
            return;
        }
        password = HashKit.sha1(password);
        if (!sysUser.getPassword().equals(password)) {
            int nowRetryTimes = retryTimes.incrementAndGet();  // 错误次数 加 1
            setAttr("username", username);
            if ((LoginRetryLimitCache.RETRY_LIMIT - nowRetryTimes) == 0) {
                setAttr("errMsg", " 账号已被锁定, " + LoginRetryLimitCache.LOCK_TIME + "分钟后可自动解锁。 ");
            } else {
                setAttr("errMsg", " 密码错误, 再错误 "
                        + (LoginRetryLimitCache.RETRY_LIMIT - nowRetryTimes) + " 次账号将被锁定" + LoginRetryLimitCache.LOCK_TIME + "分钟。");
            }
            render("login.ftl");
            return;
        }
        retryCache.put(username, new AtomicInteger()); // 密码正确缓存数清0

        if (sysUser.getUserState().equals("1")) {
            setAttr("errMsg", username + " 用户被禁用，请联系管理员。");
            render("login.ftl");
            return;
        }

        /* username password 有效 */

        // 如果选中了记住密码且cookie信息不存在，生成新的cookie 信息
        String remember = getPara("remember");
        if ("on".equals(remember) && getCookie(USERNAME_KEY) == null) {
            setCookie(USERNAME_KEY, username, 60 * 60 * 24);  // 1天
            setCookie(PASSWORD_KEY, password, 60 * 60 * 24);
        }

        sysUser.setLastLoginTime(new Date());
        sysUser.update();

        afterLogin(sysUser);

        // 登录日志
        redirect("/dashboard");
    }


    /**
     * 登录后将 用户相关信息放入到 session 中
     *
     * @param sysUser
     */
    private void afterLogin(SysUser sysUser) {
        // 登录用户信息
        setSessionAttr("sysUser", sysUser);
        // druid session 监控用
        setSessionAttr("sysUserName", sysUser.getRealName());
        //  菜单
        String roleIds = SysUserRole.dao.findRoleIdsByUserId(sysUser.getId());
        List<SysMenu> sysMenus = LoginService.findUserMenus(roleIds);
        setSessionAttr("sysUserMenu", sysMenus);
        List<String> menuCodes = new ArrayList<>();
        sysMenus.forEach(item -> menuCodes.add(item.getMenuCode()));
        setSessionAttr("menuCodes", menuCodes);
        // 按钮编码
        List<String> buttonCodes = LoginService.findUserButtons(roleIds);
        setSessionAttr("buttonCodes", buttonCodes);
        // 角色编码
        String rolecodes = SysUserRole.dao.findRoleCodesByUserId(sysUser.getId());
        setSessionAttr("roleCodes", Arrays.asList(rolecodes.split(",")));

        log.info("{} 拥有角色 ids {}", sysUser.getUsername(), roleIds);
        log.info("{} 拥有菜单 {}", sysUser.getUsername(), JSON.toJSONString(sysMenus));
        log.info("{} 拥有菜单编码 {}", sysUser.getUsername(), JSON.toJSONString(menuCodes));
        log.info("{} 拥有按钮编码 {}", sysUser.getUsername(), JSON.toJSONString(buttonCodes));
    }


    /**
     * 退出
     */
    @ActionKey("/logout")
    public void logout() {
        // 当前session 失效
        Enumeration<String> sessionAttrNames =  getSession().getAttributeNames();
        while (sessionAttrNames.hasMoreElements()) {
            removeSessionAttr(sessionAttrNames.nextElement());
        }
        // 无任何属性的 session 感觉 没必要 invalidate
        // getSession().invalidate();

        // 清理 记住密码 cookie
        setCookie(USERNAME_KEY, null, 0);
        setCookie(PASSWORD_KEY, null, 0);
        redirect("/login");
    }
}

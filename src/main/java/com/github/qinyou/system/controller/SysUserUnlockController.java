package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.guava.BaseCache;
import com.github.qinyou.common.utils.guava.CacheContainer;
import com.github.qinyou.common.utils.guava.LoginRetryLimitCache;
import com.github.qinyou.common.web.BaseController;
import com.google.common.base.Joiner;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 密码错误 n 次账号锁定 m分钟，此处为快速解锁
 */
@RequirePermission("sysUserUnlock")
public class SysUserUnlockController extends BaseController {

    public void index() {
        render("system/sysUserUnlock.ftl");
    }

    public void query() {
        Map<String, AtomicInteger> cacheAsMap = CacheContainer.getLoginRetryLimitCache().getCache().asMap();
        Set<String> userNameSet = new LinkedHashSet<>();
        cacheAsMap.forEach((K, V) -> {
            if (V.get() >= LoginRetryLimitCache.RETRY_LIMIT) {
                userNameSet.add(K);
            }
        });
        String ids = "'" + Joiner.on("','").join(userNameSet) + "'";
        List<Record> records = Db.find("select id,username,realName,job from sys_user where username in (" + ids + ")");
        renderDatagrid(records);
    }

    public void unlockAction() {
        String usernames = get("usernames");
        if (StringUtils.isEmpty(usernames)) {
            renderFail("参数不可为空");
            return;
        }

        String[] usernameAry = usernames.split(",");
        BaseCache<String, AtomicInteger> baseCache = CacheContainer.getLoginRetryLimitCache();
        for (String username : usernameAry) {
            baseCache.put(username, new AtomicInteger());
        }

        renderSuccess("解锁成功");
    }
}

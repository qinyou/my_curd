package com.github.qinyou.common.utils.guava;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录密码验证错误 缓存
 *
 * @author chuang
 */
public class LoginRetryLimitCache extends BaseCache<String, AtomicInteger> {

    public final static Long LOCK_TIME = 30L; // 30 分钟
    public final static int RETRY_LIMIT = 3;   // 尝试次数


    public LoginRetryLimitCache() {
        super(LOCK_TIME, TimeUnit.MINUTES);
    }

    @Override
    protected AtomicInteger loadData(String s) {
        return new AtomicInteger();
    }
}

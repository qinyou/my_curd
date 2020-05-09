package com.github.qinyou.common.utils.Id;

import com.github.qinyou.AppConfig;

/**
 * 生成唯一性ID算法的工具类.
 */
public class IdUtils {
    private final static SnowflakeIdWorker idWorker =
            new SnowflakeIdWorker(AppConfig.configProp.getInt("id.workerId"), AppConfig.configProp.getInt("id.datacenterId"));

    /**
     * 18 位数字型 字符串
     *
     * @return
     */
    public static String id() {
        return String.valueOf(idWorker.nextId());
    }

    /**
     * 18 位 Long 类型
     *
     * @return
     */
    public static Long idn() {
        return idWorker.nextId();
    }
}

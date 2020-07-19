package com.github.qinyou.common.utils.Id;

import com.jfinal.kit.PropKit;

/**
 * 生成唯一性ID算法的工具类.
 */
public class IdUtils {
    private final static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(PropKit.getInt("app.workerId"),
            PropKit.getInt("app.dataCenterId"));;

    /**
     * 雪花算法，String 类型id。算法有效期内，字符长度（1~19）
     */
    public static String id() {
        return String.valueOf(idWorker.nextId());
    }

    /**
     * 雪花算法，long 类型id。 算法有效期内，范围为 （ 0~2的63次方-1 ），字符长度（1~19）
     */
    public static Long idn() {
        return idWorker.nextId();
    }
}

package com.github.qinyou.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数类
 */
public abstract class RandomUtils {
    // 定义验证码字符.去除了O、I、l、、等容易混淆的字母
    private static final char[] leterAry = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'a', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            '3', '4', '5', '7', '8'};
    // 定义验证码数字
    private static final char[] numberAry = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 生成验证码
     *
     * @return
     */
    public static char getAuthCodeAllChar() {
        return leterAry[number(0, leterAry.length)];
    }

    /**
     * 生成指定长度的验证码
     *
     * @param length
     * @return
     */
    public static String getAuthCodeAll(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(getAuthCodeAllChar());
        }
        return sb.toString();
    }

    /**
     * 生成指定长度纯数字验证码，
     *
     * @param length
     * @return
     */
    public static String getAuthCodeNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(numberAry[number(0, numberAry.length)]);
        }
        return sb.toString();
    }

    /**
     * 获取UUID by jdk
     *
     * @return
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 获取UUID by jdk
     *
     * @return
     */
    public static String getUuidNoUnderline() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    /**
     * 生成 [min,max) 随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int number(int min, int max) {
        Random random = new Random();
        return min + random.nextInt(max - min);
    }

    /**
     * 产生0--number的随机数,不包括number
     *
     * @param number
     * @return
     */
    public static int number(int number) {
        Random random = new Random();
        return random.nextInt(number);
    }

    /**
     * 生成RGB随机数
     *
     * @return
     */
    public static int[] getRandomRgb() {
        int[] rgb = new int[3];
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }

    /**
     * 生成RGB随机数
     *
     * @return
     */
    public static String getRandomRgbStr() {
        int[] rgb = getRandomRgb();
        return rgb[0] + "," + rgb[1] + "," + rgb[2];
    }

}

package com.github.qinyou.common.utils;


import com.github.qinyou.AppConfig;
import com.google.common.base.Strings;
import com.jfinal.kit.AesKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 字符串工具
 */
@Slf4j
public class StringUtils {
    private final static String AES_KEY = AppConfig.configProp.get("security.aesKey");

    /**
     * 判断字符串是 null 或者  空格
     *
     * @param str 需要验证的字符串
     * @return true 是，false 否
     */
    public static boolean isEmpty(String str) {
        return Strings.isNullOrEmpty(str);
    }

    /**
     * 字符串 不是 null 且 不是 空格
     *
     * @param str 需要验证的字符串
     * @return true 是，false 否
     */
    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰转下划线
     *
     * @param camelCaseStr 驼峰格式字符串
     * @return 下划线格式字符串
     */
    public static String toUnderline(String camelCaseStr) {
        if (camelCaseStr == null || "".equals(camelCaseStr.trim())) {
            return "";
        }
        int len = camelCaseStr.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = camelCaseStr.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 字符串先aes 加密为 byte[],再转 hex String
     *
     * @return
     */
    public static String encryptAesHex(String str) {
        return bytesToHexString(AesKit.encrypt(str, AES_KEY));
    }

    /**
     * hex 字符串先转 byte[], 再aes 解密
     *
     * @param str
     * @return
     */
    public static String decryptAesHex(String str) {
        String resultStr = null;
        try {
            resultStr = AesKit.decryptToStr(hexStringToBytes(str), AES_KEY);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 字符串包含
     * 全部包含 返回true，否则返回false
     *
     * @param sourceStr
     * @param string
     * @return
     */
    public static boolean asListAndContains(String sourceStr, String string) {
        boolean flag = true;
        String[] strAry = string.split(",");
        List<String> sourceStrAry = Arrays.asList(sourceStr.split(","));
        for (String str : strAry) {
            if (!sourceStrAry.contains(str)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 字符串包含
     * 任一包含 返回 true, 全部不包含返回 false
     *
     * @param sourceStr
     * @param string
     * @return
     */
    public static boolean asListOrContains(String sourceStr, String string) {
        boolean flag = false;
        String[] strAry = string.split(",");
        List<String> sourceStrAry = Arrays.asList(sourceStr.split(","));
        for (String str : strAry) {
            if (sourceStrAry.contains(str)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}

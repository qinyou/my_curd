package com.github.qinyou.common.ws;

import com.github.qinyou.common.utils.StringUtils;
import com.jfinal.kit.AesKit;

/**
 * 用户id 加密工具
 *
 * @author chuang
 * @date 2018/9/10
 */
public class UserIdEncryptUtils {
    public final static String CURRENT_USER_ID_AESKEY = "uoz01RNH3B0APDhpS2SUyN-CgE6eAspO";
    public final static String OTHER_USERS_ID_AESKEY = "TNDyNdSF04AiRRKbvlS1_wkTBGVeFZ-K";

    /**
     * 加密
     *
     * @param str    未加密字符串
     * @param aesKey
     * @return
     */
    public static String encrypt(String str, String aesKey) {
        byte[] bytes = AesKit.encrypt(str, aesKey);
        return StringUtils.bytesToHexString(bytes);
    }

    /**
     * 解密
     *
     * @param str    已加密字符串
     * @param aesKey
     * @return
     */
    public static String decrypt(String str, String aesKey) {
        byte[] bytes = StringUtils.hexStringToBytes(str);
        return AesKit.decryptToStr(bytes, aesKey);
    }
}

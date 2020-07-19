package com.github.qinyou.common.utils.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * jwt token 工具
 *
 * @author zhangchuang
 */
public class JwtUtils {

    private static final String ISS ;                 // 签名 签发者
    private static final String SECRET ;              // 签名 密钥
    private static final Long EXPIRATION_TIME_VALUE ; // 过期时间 (单位为秒)
    static{
        ISS = PropKit.get("jwt.iss");
        SECRET = PropKit.get("jwt.secret");
        EXPIRATION_TIME_VALUE = 60L * 60 * 24 * PropKit.getInt("jwt.exp");
    }

    /**
     * 生成签名
     *
     * @param username       用户名
     * @param roleList       角色集合
     * @param permissionList 权限集合
     * @return
     */
    public static String buildToken(String username, List<String> roleList, List<String> permissionList) {
        // HS256签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // 构造payload
        long nowSeconds = System.currentTimeMillis() / 1000;
        JSONObject payload = new JSONObject();
        payload.put("iss", ISS);                   // 签发者
        payload.put("iat", nowSeconds);             // 签发时间
        payload.put("exp", nowSeconds + EXPIRATION_TIME_VALUE);  // 过期时间

        payload.put("username", username);
        if (roleList == null) {
            payload.put("roleList", new ArrayList<>());
        }
        if (permissionList == null) {
            payload.put("permissionList", new ArrayList<>());
        }
        JwtBuilder builder = Jwts.builder().setPayload(payload.toJSONString())
                .signWith(signatureAlgorithm, signingKey);

        return "Bearer "+builder.compact();
    }


    /**
     * token 解析
     * ExpiredJwtException    token 过期
     * MalformedJwtException  token 错误
     * SignatureException     token 错误
     *
     * @param token token字符串
     * @return
     */
    public static UserClaim parseToken(String token) {
        token = token.substring(7);
        //.setAllowedClockSkewSeconds(100) // 过期后 后 延后100s 内仍能解析
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(token)
                .getBody();

        UserClaim userClaim = new UserClaim();
        userClaim.setUsername((String) claims.get("username"));
        userClaim.setRoleList((List<String>) claims.get("roleList"));
        userClaim.setPermissionList((List<String>) claims.get("permissionList"));
        userClaim.setIss(claims.getIssuer());
        userClaim.setIat(claims.getIssuedAt());
        userClaim.setExp(claims.getExpiration());

        return userClaim;
    }

    public static void main(String[] args) {
        String token = buildToken("chuang", null, null);
        UserClaim userClaim = parseToken(token);
        System.out.println(JSON.toJSONString(userClaim));
    }
}

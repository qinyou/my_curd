package com.github.qinyou.common.utils;

import com.github.qinyou.system.model.SysUser;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebUtils {

    /**
     * 获取 http请求  ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得session 当前用户 用户名
     *
     * @param controller
     * @return
     */
    public static String getSessionUsername(Controller controller) {
        SysUser sysUser = controller.getSessionAttr("sysUser");
        return sysUser == null ? "debug" : sysUser.getUsername();
    }


    /**
     * 当前登录的系统用户
     *
     * @return
     */
    public static SysUser getSysUser(Controller controller) {
        return controller.getSessionAttr("sysUser");
    }


    /**
     * 获得当前路径
     *
     * @return
     */
    public static String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }


    /**
     * 中文文件 下载编码，多浏览器适配
     *
     * @param request
     * @param fileName
     * @return
     */
    public static String buildDownname(HttpServletRequest request, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        try {
            String encodedFileName = URLEncoder.encode(fileName, "UTF8");
            if (userAgent == null) {
                return "filename=\"" + encodedFileName + "\"";
            } else {
                userAgent = userAgent.toLowerCase();
                if (userAgent.contains("msie")) {
                    return "filename=\"" + encodedFileName + "\"";
                } else if (userAgent.contains("opera")) {
                    return "filename*=UTF-8''" + encodedFileName;
                } else if (!userAgent.contains("safari") && !userAgent.contains("applewebkit") && !userAgent.contains("chrome")) {
                    return userAgent.contains("mozilla") ? "filename*=UTF-8''" + encodedFileName : "filename=\"" + encodedFileName + "\"";
                } else {
                    return "filename=\"" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1") + "\"";
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 用户组织机构列表
     * @param username
     * @return
     */
    public static List<Map<String,String>> userOrgs(String username){
        List<Map<String,String>> list = new ArrayList<>();
        String sql = "select c.id,c.orgName,c.pid from sys_user_org a, sys_user b, sys_org c where a.sysUserId = b.id and a.sysOrgId = c.id and b.username = ?";
        List<Record> orgs = Db.find(sql,username);
        for(Record org: orgs){
            Map<String,String> item = new HashMap<>();
            item.put("id",org.getStr("id"));
            item.put("name",buildOrgName(org.getStr("orgName"),org.getStr("pid")));
            list.add(item);
        }
        return list;
    }


    /**
     * 机构完整名
     * @param name 机构名
     * @param pid  机构pid
     * @return
     */
    public static String buildOrgName(String name, String pid){
        if("0".equals(pid)){
            return  name;
        }else{
            String sql = "select orgName,pid from sys_org where id = ?";
            Record record = Db.findFirst(sql,pid);
            if(record==null){
                return name;
            }else{
                name = record.getStr("orgName")+"/"+name;
                return buildOrgName(name,record.getStr("pid"));
            }
        }
    }

}

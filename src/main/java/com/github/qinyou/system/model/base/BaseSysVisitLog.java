package com.github.qinyou.system.model.base;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_visit_log  系统访问日志
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysVisitLog<M extends BaseSysVisitLog<M>> extends Model<M> implements IBean {

    //----------- 导出 excel 需要， 依赖 get 方法，也可考虑 model 转 method
    @Excel(name = "访问人", mergeVertical = false, height = 10, width = 30)
    private String sysUser;
    @Excel(name = "访问时间", height = 10, width = 40, databaseFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy年MM月dd日 HH时mm分ss秒")
    private Date createTime;
    @Excel(name = "ip地址", height = 10, width = 30)
    private String sysUserIp;
    @Excel(name = "请求地址", height = 10, width = 30)
    private String url;
    @Excel(name = "请求类型", height = 10, width = 30)
    private String requestType;
    // 非本表中字段 get 方法
    //------------ 导出excel 必须

    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 操作人
    public String getSysUser() {
        return getStr("sysUser");
    }

    public M setSysUser(String sysUser) {
        set("sysUser", sysUser);
        return (M) this;
    }


    // 操作人ip
    public String getSysUserIp() {
        return getStr("sysUserIp");
    }

    public M setSysUserIp(String sysUserIp) {
        set("sysUserIp", sysUserIp);
        return (M) this;
    }


    // 访问地址
    public String getUrl() {
        return getStr("url");
    }

    public M setUrl(String url) {
        set("url", url);
        return (M) this;
    }


    // 创建时间
    public Date getCreateTime() {
        return get("createTime");
    }

    public M setCreateTime(Date createTime) {
        set("createTime", createTime);
        return (M) this;
    }


    // 访问类型,GET POST PUT DELETE
    public String getRequestType() {
        return getStr("requestType");
    }

    public M setRequestType(String requestType) {
        set("requestType", requestType);
        return (M) this;
    }


    // 参数
    public String getParam() {
        return getStr("param");
    }

    public M setParam(String param) {
        set("param", param);
        return (M) this;
    }
}

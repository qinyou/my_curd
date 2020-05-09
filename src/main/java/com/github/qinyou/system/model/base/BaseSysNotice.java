package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_notice  通知消息
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysNotice<M extends BaseSysNotice<M>> extends Model<M> implements IBean {


    // 系统主键
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 通知类型编码
    public String getTypeCode() {
        return getStr("typeCode");
    }

    public M setTypeCode(String typeCode) {
        set("typeCode", typeCode);
        return (M) this;
    }


    // 标题
    public String getTitle() {
        return getStr("title");
    }

    public M setTitle(String title) {
        set("title", title);
        return (M) this;
    }


    // 内容
    public String getContent() {
        return getStr("content");
    }

    public M setContent(String content) {
        set("content", content);
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


    // 过期时间
    public Date getExpiryTime() {
        return get("expiryTime");
    }

    public M setExpiryTime(Date expiryTime) {
        set("expiryTime", expiryTime);
        return (M) this;
    }


    // 必死时间
    public Date getDeadTime() {
        return get("deadTime");
    }

    public M setDeadTime(Date deadTime) {
        set("deadTime", deadTime);
        return (M) this;
    }
}

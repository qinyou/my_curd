package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_notice_detail  通知消息从表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysNoticeDetail<M extends BaseSysNoticeDetail<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 通知id
    public String getSysNoticeId() {
        return getStr("sysNoticeId");
    }

    public M setSysNoticeId(String sysNoticeId) {
        set("sysNoticeId", sysNoticeId);
        return (M) this;
    }


    // 消息发送人
    public String getSender() {
        return getStr("sender");
    }

    public M setSender(String sender) {
        set("sender", sender);
        return (M) this;
    }


    // 消息接收人
    public String getReceiver() {
        return getStr("receiver");
    }

    public M setReceiver(String receiver) {
        set("receiver", receiver);
        return (M) this;
    }


    // 是否阅读YN
    public String getHasRead() {
        return getStr("hasRead");
    }

    public M setHasRead(String hasRead) {
        set("hasRead", hasRead);
        return (M) this;
    }


    // 阅读时间
    public Date getReadTime() {
        return get("readTime");
    }

    public M setReadTime(Date readTime) {
        set("readTime", readTime);
        return (M) this;
    }
}

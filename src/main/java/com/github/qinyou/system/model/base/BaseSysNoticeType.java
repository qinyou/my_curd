package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_notice_type  通知分类
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysNoticeType<M extends BaseSysNoticeType<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 分类
    public String getCate() {
        return getStr("cate");
    }

    public M setCate(String cate) {
        set("cate", cate);
        return (M) this;
    }


    // 名称
    public String getTypeName() {
        return getStr("typeName");
    }

    public M setTypeName(String typeName) {
        set("typeName", typeName);
        return (M) this;
    }


    // 消息编码
    public String getTypeCode() {
        return getStr("typeCode");
    }

    public M setTypeCode(String typeCode) {
        set("typeCode", typeCode);
        return (M) this;
    }


    // LOGO图标
    public String getLogo() {
        return getStr("logo");
    }

    public M setLogo(String logo) {
        set("logo", logo);
        return (M) this;
    }


    // 消息模板
    public String getTpl() {
        return getStr("tpl");
    }

    public M setTpl(String template) {
        set("tpl", template);
        return (M) this;
    }


    // 备注
    public String getRemark() {
        return getStr("remark");
    }

    public M setRemark(String remark) {
        set("remark", remark);
        return (M) this;
    }


    // 过期天数
    public Integer getUntilExpiryDay() {
        return getInt("untilExpiryDay");
    }

    public M setUntilExpiryDay(Integer untilExpiryDay) {
        set("untilExpiryDay", untilExpiryDay);
        return (M) this;
    }


    // 存活天数
    public Integer getUntilDeadDay() {
        return getInt("untilDeadDay");
    }

    public M setUntilDeadDay(Integer untilDeadDay) {
        set("untilDeadDay", untilDeadDay);
        return (M) this;
    }


    // 创建人
    public String getCreater() {
        return getStr("creater");
    }

    public M setCreater(String creater) {
        set("creater", creater);
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


    // 最后修改人
    public String getUpdater() {
        return getStr("updater");
    }

    public M setUpdater(String updater) {
        set("updater", updater);
        return (M) this;
    }


    // 最后修改时间
    public Date getUpdateTime() {
        return get("updateTime");
    }

    public M setUpdateTime(Date updateTime) {
        set("updateTime", updateTime);
        return (M) this;
    }
}

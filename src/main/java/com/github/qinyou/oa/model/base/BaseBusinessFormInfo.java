package com.github.qinyou.oa.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: business_form_info  业务表定义信息
 *
 * @author chuang
 * @since 2019-08-22 20:48:59
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBusinessFormInfo<M extends BaseBusinessFormInfo<M>> extends Model<M> implements IBean {


    // 主键
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 分类id
    public String getCategoryId() {
        return getStr("categoryId");
    }

    public M setCategoryId(String categoryId) {
        set("categoryId", categoryId);
        return (M) this;
    }


    // 名字
    public String getName() {
        return getStr("name");
    }

    public M setName(String name) {
        set("name", name);
        return (M) this;
    }


    // 图标
    public String getIcon() {
        return getStr("icon");
    }

    public M setIcon(String icon) {
        set("icon", icon);
        return (M) this;
    }


    // 描述信息
    public String getInfo() {
        return getStr("info");
    }

    public M setInfo(String info) {
        set("info", info);
        return (M) this;
    }


    // 表单数据表名
    public String getFormName() {
        return getStr("formName");
    }

    public M setFormName(String formName) {
        set("formName", formName);
        return (M) this;
    }


    // 流程定义key
    public String getProcessKey() {
        return getStr("processKey");
    }

    public M setProcessKey(String processKey) {
        set("processKey", processKey);
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


    // 创建人
    public String getCreater() {
        return getStr("creater");
    }

    public M setCreater(String creater) {
        set("creater", creater);
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


    // 最后修改人
    public String getUpdater() {
        return getStr("updater");
    }

    public M setUpdater(String updater) {
        set("updater", updater);
        return (M) this;
    }
}

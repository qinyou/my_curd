package com.github.qinyou.process.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: act_form_tpl  表单模板
 *
 * @author chuang
 * @since 2020-06-30 16:48:40
 */
@SuppressWarnings({"serial", "unchecked", "Duplicates"})
public abstract class BaseActFormTpl<M extends BaseActFormTpl<M>> extends Model<M> implements IBean {


    // 主键
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 表单key
    public String getFormKey() {
        return getStr("formKey");
    }

    public M setFormKey(String formKey) {
        set("formKey", formKey);
        return (M) this;
    }


    // 版本号
    public Integer getFormVersion() {
        return getInt("formVersion");
    }

    public M setFormVersion(Integer formVersion) {
        set("formVersion", formVersion);
        return (M) this;
    }


    // 表单文本
    public String getTemplate() {
        return getStr("template");
    }

    public M setTemplate(String template) {
        set("template", template);
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


    // 涉及的插件
    public String getPlugins() {
        return getStr("plugins");
    }

    public M setPlugins(String plugins) {
        set("plugins", plugins);
        return (M) this;
    }

    //状态
    public String getState(){
        return getStr("state");
    }
    public M setState(String state){
        set("state", state);
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

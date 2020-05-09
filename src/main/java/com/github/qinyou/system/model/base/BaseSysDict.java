package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_dict  字典表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysDict<M extends BaseSysDict<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 分类编码
    public String getGroupCode() {
        return getStr("groupCode");
    }

    public M setGroupCode(String groupCode) {
        set("groupCode", groupCode);
        return (M) this;
    }


    // 文本
    public String getDictLabel() {
        return getStr("dictLabel");
    }

    public M setDictLabel(String dictLabel) {
        set("dictLabel", dictLabel);
        return (M) this;
    }


    // 文本值
    public String getDictValue() {
        return getStr("dictValue");
    }

    public M setDictValue(String dictValue) {
        set("dictValue", dictValue);
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


    // 排序号
    public Integer getSortNum() {
        return getInt("sortNum");
    }

    public M setSortNum(Integer sortNum) {
        set("sortNum", sortNum);
        return (M) this;
    }


    // 删除标志，X 已删除
    public String getDelFlag() {
        return getStr("delFlag");
    }

    public M setDelFlag(String delFlag) {
        set("delFlag", delFlag);
        return (M) this;
    }


    // 状态，on启用off禁用
    public String getState() {
        return getStr("state");
    }

    public M setState(String state) {
        set("state", state);
        return (M) this;
    }
}

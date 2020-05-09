package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_button  菜单按钮
 *
 * @author zhangchuang
 * @since 2019-02-28 19:22:25
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysButton<M extends BaseSysButton<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 菜单id
    public String getSysMenuId() {
        return getStr("sysMenuId");
    }

    public M setSysMenuId(String sysMenuId) {
        set("sysMenuId", sysMenuId);
        return (M) this;
    }


    // 按钮编码
    public String getButtonCode() {
        return getStr("buttonCode");
    }

    public M setButtonCode(String buttonCode) {
        set("buttonCode", buttonCode);
        return (M) this;
    }


    // 按钮说明
    public String getButtonTxt() {
        return getStr("buttonTxt");
    }

    public M setButtonTxt(String buttonTxt) {
        set("buttonTxt", buttonTxt);
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


    // 最后更新人
    public String getUpdater() {
        return getStr("updater");
    }

    public M setUpdater(String updater) {
        set("updater", updater);
        return (M) this;
    }


    // 最后更新时间
    public Date getUpdateTime() {
        return get("updateTime");
    }

    public M setUpdateTime(Date updateTime) {
        set("updateTime", updateTime);
        return (M) this;
    }
}

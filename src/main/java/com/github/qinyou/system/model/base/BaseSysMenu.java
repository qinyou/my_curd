package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_menu  系统菜单
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysMenu<M extends BaseSysMenu<M>> extends Model<M> implements IBean {


    // 主键ID
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 菜单名称
    public String getMenuName() {
        return getStr("menuName");
    }

    public M setMenuName(String menuName) {
        set("menuName", menuName);
        return (M) this;
    }

    // 菜单编码
    public String getMenuCode() {
        return getStr("menuCode");
    }

    public M setMenuCode(String menuCode) {
        set("menuCode", menuCode);
        return (M) this;
    }


    // 菜单地址
    public String getUrl() {
        return getStr("url");
    }

    public M setUrl(String url) {
        set("url", url);
        return (M) this;
    }


    // 菜单图标
    public String getIcon() {
        return getStr("icon");
    }

    public M setIcon(String icon) {
        set("icon", icon);
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


    // 父ID
    public String getPid() {
        return getStr("pid");
    }

    public M setPid(String pid) {
        set("pid", pid);
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

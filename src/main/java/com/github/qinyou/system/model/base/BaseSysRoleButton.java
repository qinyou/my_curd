package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_role_button  角色 菜单按钮中间表
 *
 * @author zhangchuang
 * @since 2019-02-28 19:22:25
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysRoleButton<M extends BaseSysRoleButton<M>> extends Model<M> implements IBean {


    // 角色id
    public String getSysRoleId() {
        return getStr("sysRoleId");
    }

    public M setSysRoleId(String sysRoleId) {
        set("sysRoleId", sysRoleId);
        return (M) this;
    }


    // 菜单按钮id
    public String getSysButtonId() {
        return getStr("sysButtonId");
    }

    public M setSysButtonId(String sysButtonId) {
        set("sysButtonId", sysButtonId);
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
}

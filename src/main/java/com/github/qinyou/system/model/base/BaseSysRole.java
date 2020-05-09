package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_role  角色
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysRole<M extends BaseSysRole<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 角色名称
    public String getRoleName() {
        return getStr("roleName");
    }

    public M setRoleName(String roleName) {
        set("roleName", roleName);
        return (M) this;
    }


    // 角色编码
    public String getRoleCode() {
        return getStr("roleCode");
    }

    public M setRoleCode(String roleCode) {
        set("roleCode", roleCode);
        return (M) this;
    }


    // 角色描述
    public String getRoleDesc() {
        return getStr("roleDesc");
    }

    public M setRoleDesc(String roleDesc) {
        set("roleDesc", roleDesc);
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

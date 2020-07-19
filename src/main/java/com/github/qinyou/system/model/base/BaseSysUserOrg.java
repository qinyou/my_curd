package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;
/**
 * Generated baseModel
 * DB table: sys_user_org  用户组织机构中间表
 * @author chuang
 * @since 2020-03-01 18:25:41
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysUserOrg<M extends BaseSysUserOrg<M>> extends Model<M> implements IBean {
     // 用户id
     public String getSysUserId() {
        return getStr("sysUserId");
     }
     public M setSysUserId(String sysUserId) {
        set("sysUserId", sysUserId);
        return (M)this;
     }

     // 部门id
     public String getSysOrgId() {
        return getStr("sysOrgId");
     }
     public M setSysOrgId(String sysOrgId) {
        set("sysOrgId", sysOrgId);
        return (M)this;
     }
}

package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysUserOrg;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_user_org  用户组织机构中间表
 * @author chuang
 * @since 2020-03-01 18:25:41
 */
@SuppressWarnings("serial")
public class SysUserOrg extends BaseSysUserOrg<SysUserOrg> {
    public static final SysUserOrg dao = new SysUserOrg().dao();

    /**
     *  通过中间表查询用户信息
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysUserOrg> pageUser(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.*, (select GROUP_CONCAT(e.roleName) from sys_user_role d,sys_role e where a.id = d.sysUserId and e.id =d.sysRoleId) as roles ";
        String sqlExceptSelect = " from sys_user a, sys_user_org b,sys_org c  " ;
        sqlExceptSelect += " where a.id = b.sysUserId and b.sysOrgId = c.id and a.delFlag is  null ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        sqlExceptSelect +=" group by a.id ";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }
}

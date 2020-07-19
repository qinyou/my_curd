package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysRoleButton;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_role_button  角色 菜单按钮中间表
 *
 * @author zhangchuang
 * @since 2019-02-28 19:22:25
 */
@SuppressWarnings("serial")
public class SysRoleButton extends BaseSysRoleButton<SysRoleButton> {
    public static final SysRoleButton dao = new SysRoleButton().dao();

    // 通过中间表查询角色数据
    public Page<SysRoleButton> pageRole(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysButtonId,a.creater,a.createTime, b.roleName,b.roleCode ";
        String sqlExceptSelect = " from sys_role_button a, sys_role b  where a.sysRoleId = b.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

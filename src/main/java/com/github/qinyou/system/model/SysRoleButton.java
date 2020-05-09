package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysRoleButton;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

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


    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     */
    public List<SysRoleButton> findByRoleId(String roleId) {
        String sql = "select sysButtonId from sys_role_button where sysRoleId = ?";
        return find(sql, roleId);
    }

    /**
     * 分页查询，角色关联按钮数据
     *
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysRoleButton> pageWithRoleInfo(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysButtonId,a.creater,a.createTime, b.roleName,b.roleCode ";
        String sqlExceptSelect = " from sys_role_button a, sys_role b  where a.sysRoleId = b.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

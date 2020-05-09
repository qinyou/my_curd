package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysRoleMenu;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: sys_role_menu  角色菜单中间表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysRoleMenu extends BaseSysRoleMenu<SysRoleMenu> {
    public static final SysRoleMenu dao = new SysRoleMenu().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysRoleMenu> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_role_menu  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 分页查询，角色关联菜单数据
     *
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysRoleMenu> pageWithRoleInfo(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysMenuId,a.creater,a.createTime, b.roleName,b.roleCode ";
        String sqlExceptSelect = " from sys_role_menu a, sys_role b  where a.sysRoleId = b.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     */
    public List<SysRoleMenu> findByRoleId(String roleId) {
        String sql = "select sysMenuId from sys_role_menu where sysRoleId = ?";
        return find(sql, roleId);
    }
}

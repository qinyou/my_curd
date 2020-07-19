package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysRoleMenu;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
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

    // 通过中间表查询 角色数据
    public Page<SysRoleMenu> pageRole(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysMenuId,a.creater,a.createTime, b.roleName,b.roleCode ";
        String sqlExceptSelect = " from sys_role_menu a, sys_role b  where a.sysRoleId = b.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    // 查询某角色 相关 菜单集合
    public List<SysRoleMenu> findByRoleId(String roleId) {
        String sql = "select sysMenuId from sys_role_menu where sysRoleId = ?";
        return find(sql, roleId);
    }

    /**
     * 通过 角色ids (数字数组，逗号分隔字符串) 查询菜单
     *
     * @param roleIds
     * @return
     */
    public List<SysMenu> findMenusByRoleIds(String roleIds) {
        List<SysMenu> result = new ArrayList<>();
        if (StringUtils.notEmpty(roleIds)) {
            String sql = "select a.* from sys_menu a, sys_role_menu b where a.id = b.sysMenuId and b.sysRoleId in ('" + roleIds + "')";
            result = SysMenu.dao.find(sql);
        }
        return result;
    }
}

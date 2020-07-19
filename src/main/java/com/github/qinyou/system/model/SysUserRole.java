package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysUserRole;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Generated model
 * DB: sys_user_role  用户角色中间表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysUserRole extends BaseSysUserRole<SysUserRole> {
    public static final SysUserRole dao = new SysUserRole().dao();

    // 通过中间表 分页查询角色信息
    public Page<SysUserRole> pageRole(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysUserId,b.roleName, b.roleCode, b.roleDesc, a.creater,a.createTime ";
        String sqlExceptSelect = " from sys_user_role a, sys_role b  where a.sysRoleId = b.id ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        sqlExceptSelect += " order by b.sortNum asc ";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    // 通过中间表 分页查询用户数据
    public Page<SysUserRole> pageUser(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.sysRoleId,a.sysUserId,a.creater,a.createTime, b.username,b.realName,b.job, b.userState ";
        String sqlExceptSelect = " from sys_user_role a, sys_user b, sys_role c  where a.sysUserId = b.id and a.sysRoleId = c.id and b.delFlag is null ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 通过用户id 查询 角色id
     *
     * @param userId
     * @return
     */
    public String findRoleIdsByUserId(String userId) {
        String sql = " select GROUP_CONCAT(c.id) as roleIds" +
                "  from sys_user a, sys_user_role b,sys_role c " +
                "  where a.id = b.sysUserId and b.sysRoleId = c.id  and a.id = ? ";
        Record record = Db.findFirst(sql, userId);
        return record.getStr("roleIds");
    }

    /**
     * 通过用户查询角色编码
     *
     * @param userId
     * @return
     */
    public String findRoleCodesByUserId(String userId) {
        String sql = "select GROUP_CONCAT(c.roleCode) as roleCodes" +
                "  from sys_user a, sys_user_role b,sys_role c " +
                "  where a.id = b.sysUserId and b.sysRoleId = c.id  and a.id = ? ";
        Record record = Db.findFirst(sql, userId);
        return record.getStr("roleCodes");
    }

    /**
     * 通过用户名查询用户角色
     * @param username
     * @return
     */
    public List<Record> findRolesByUsername(String username){
        String sql = "select roleName,roleCode from sys_role a, sys_user_role b, sys_user c " +
                " where a.id = b.sysRoleId  and b.sysUserId = c.id and c.username =? order by a.sortNum";
        List<Record> roles = Db.find(sql,username);
        return roles;
    }
}

package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysUser;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_user  系统用户表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial","Duplicates"})
public class SysUser extends BaseSysUser<SysUser> {
    public static final SysUser dao = new SysUser().dao();

    // 分页查询用户信息
    public Page<SysUser> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.*, " +
                " (select group_concat(y.orgName) as orgNames from sys_user_org x,sys_org y where x.sysOrgId = y.id and x.sysUserId = a.id) as orgNames ";
        String sqlExceptSelect = " from sys_user a" ;
        sqlExceptSelect += " where a.delFlag is  null ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    // 通过用户名和密码查询
    public SysUser findByUsernameAndPassword(String username, String password) {
        String sql = "select * from sys_user where username = ? and password = ? and delFlag is null";
        return findFirst(sql, username, password);
    }

    // 通过用户名查询
    public SysUser findByUsername(String username) {
        String sql = "select * from sys_user where username= ?  and delFlag is null";
        return findFirst(sql, username);
    }

    // 通过用户名 查询 用户的详细信息（包括组织机构等详细信息)
    public SysUser findInfoByUsername(String username) {
        String sqlSelect = " select a.*, " +
                "(select group_concat(y.orgName) as orgNames from sys_user_org x,sys_org y where x.sysOrgId = y.id and x.sysUserId = a.id) as orgNames, " +
                "(select GROUP_CONCAT(c.roleName) from sys_user_role b,sys_role c where a.id = b.sysUserId and c.id = b.sysRoleId) as roleNames ";
        String sqlExceptSelect = " from sys_user a " ;
        sqlExceptSelect += " where a.username = ? ";
        return this.findFirst(sqlSelect + sqlExceptSelect, username);
    }
}

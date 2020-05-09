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
@SuppressWarnings("serial")
public class SysUser extends BaseSysUser<SysUser> {
    public static final SysUser dao = new SysUser().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysUser> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.*, b.orgName  ,d.dictLabel as jobLevelText, e.dictLabel as userStateText ";
        String sqlExceptSelect = " from sys_user a " +
                " left join sys_org b on a.orgId = b.id  " +
                " left join sys_dict d on d.groupCode='jobLevel' and a.jobLevel = d.dictValue " +
                " left join sys_dict e on e.groupCode='userState' and a.userState = e.dictValue";
        sqlExceptSelect += " where a.delFlag is  null ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }


    /**
     * 根据用户名和 密码查询
     *
     * @param username
     * @param password
     * @return
     */
    public SysUser findByUsernameAndPassword(String username, String password) {
        String sql = "select * from sys_user where username = ? and password = ?";
        return findFirst(sql, username, password);
    }

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    public SysUser findByUsername(String username) {
        String sql = "select * from sys_user where username= ? ";
        return findFirst(sql, username);
    }

    /**
     * 根据用户名查询用户完整信息
     *
     * @param username
     * @return
     */
    public SysUser findInfoByUsername(String username) {
        String sqlSelect = " select a.*, b.orgName , d.dictLabel as jobLevelText, e.dictLabel as userStateText ";
        String sqlExceptSelect = " from sys_user a " +
                " left join sys_org b on a.orgId = b.id  " +
                " left join sys_dict d on d.groupCode='jobLevel' and a.jobLevel = d.dictValue " +
                " left join sys_dict e on e.groupCode='userState' and a.userState= e.dictValue";
        sqlExceptSelect += " where a.username = ? ";
        return this.findFirst(sqlSelect + sqlExceptSelect, username);
    }


    /**
     * 根据角色查询 用户信息
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysUser> pageUserByRole(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.*, b.orgName  ,d.dictLabel as jobLevelText, e.dictLabel as userStateText ";
        String sqlExceptSelect = " from sys_user a " +
                " left join sys_org b on a.orgId = b.id  " +
                " left join sys_dict d on d.groupCode='jobLevel' and a.jobLevel = d.dictValue " +
                " left join sys_dict e on e.groupCode='userState' and a.userState = e.dictValue " +
                " INNER JOIN sys_user_role f ON f.sysUserId = a.id\n" +
                " INNER JOIN sys_role h ON h.id = f.sysRoleId ";
        sqlExceptSelect += " where a.delFlag is  null ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " and " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }
}

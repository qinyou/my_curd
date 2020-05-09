package com.github.qinyou.system.model;

import com.github.qinyou.system.model.base.BaseSysNoticeTypeSysRole;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Generated model
 * DB: sys_notice_type_sys_role  系统通知类型角色中间表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysNoticeTypeSysRole extends BaseSysNoticeTypeSysRole<SysNoticeTypeSysRole> {
    public static final SysNoticeTypeSysRole dao = new SysNoticeTypeSysRole().dao();


    /**
     * 分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysNoticeTypeSysRole> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select a.*,c.cate,c.typeName,c.typeCode,  b.roleName,b.roleCode,b.roleDesc ";
        String sqlExceptSelect = " from sys_notice_type_sys_role a " +
                " left join sys_role b on a.sysRoleId = b.id " +
                " left join sys_notice_type c on a.sysNoticeTypeId = c.id ";
        if (StrKit.notBlank(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect += " order by b.sortNum ";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }


    /**
     * 通过通知类型id 查询关联角色 再查询到相关联的用户
     *
     * @param noticeTypeId
     * @return
     */
    public List<Record> findUserIdsByNoticeType(String noticeTypeId) {
        List<Record> userIds = Db.find(" SELECT d.sysUserId " +
                "FROM " +
                "( SELECT a.sysRoleId FROM sys_notice_type_sys_role a, sys_role b  WHERE a.sysRoleId = b.id  and a.sysNoticeTypeId = ? ) aa " +
                "LEFT JOIN sys_user_role d ON aa.sysRoleId = d.sysRoleId", noticeTypeId);
        return userIds;
    }
}

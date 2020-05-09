package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysRole;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_role  角色
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysRole extends BaseSysRole<SysRole> {
    public static final SysRole dao = new SysRole().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysRole> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_role  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect+=" order by sortNum";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

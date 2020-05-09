package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysVisitLog;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: sys_visit_log  系统访问日志
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysVisitLog extends BaseSysVisitLog<SysVisitLog> {
    public static final SysVisitLog dao = new SysVisitLog().dao();

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param where
     * @return
     */
    public Page<SysVisitLog> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select id,sysUser,sysUserIp,url,createTime,requestType ";
        String sqlExceptSelect = " from sys_visit_log  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect += " order by createTime desc ";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }


    public Long findCountByWhere(String where) {
        String sql = " select count(1) as c from sys_visit_log ";
        if (StringUtils.notEmpty(where)) {
            sql += " where " + where;
        }
        return findFirst(sql).getLong("c");
    }

    /**
     * 根据 where 条件查询
     *
     * @param where
     * @return
     */
    public List<SysVisitLog> findByWhere(String where) {
        String sql = " select * from sys_visit_log ";
        if (StringUtils.notEmpty(where)) {
            sql += " where " + where;
        }
        return find(sql);
    }

}

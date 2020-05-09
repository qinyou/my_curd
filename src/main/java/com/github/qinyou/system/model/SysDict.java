package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysDict;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: sys_dict  字典表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysDict extends BaseSysDict<SysDict> {
    public static final SysDict dao = new SysDict().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysDict> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_dict  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect += " order by sortNum asc ";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 根据分组编码查询
     *
     * @param groupCode
     * @return
     */
    public List<SysDict> findAllByGroupCode(String groupCode) {
        String sql = "select dictLabel as label, dictValue as value from sys_dict where groupCode = ?  order by sortNum ";
        return find(sql, groupCode);
    }

    public List<SysDict> findListByGroupAndState(String groupCode, String state) {
        String sql = "select dictLabel as label, dictValue as value from sys_dict where groupCode = ? and state = ? and delFlag is null  order by sortNum ";
        return find(sql, groupCode, state);
    }

}

package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysDictGroup;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_dict_group  字典分组表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysDictGroup extends BaseSysDictGroup<SysDictGroup> {
    public static final SysDictGroup dao = new SysDictGroup().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysDictGroup> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_dict_group  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 根据 编码查询
     *
     * @param groupCode
     * @return
     */
    public SysDictGroup findByGroupCode(String groupCode) {
        String sql = "select * from sys_dict_group where  groupCode = ?";
        return findFirst(sql, groupCode);
    }

}

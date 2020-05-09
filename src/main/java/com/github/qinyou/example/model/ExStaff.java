package com.github.qinyou.example.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.example.model.base.BaseExStaff;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: ex_staff  一线员工
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings("serial")
public class ExStaff extends BaseExStaff<ExStaff> {
    public static final ExStaff dao = new ExStaff().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<ExStaff> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from ex_staff  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

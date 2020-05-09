package com.github.qinyou.example.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.example.model.base.BaseExStaffFamily;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: ex_staff_family  员工家人
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings("serial")
public class ExStaffFamily extends BaseExStaffFamily<ExStaffFamily> {
    public static final ExStaffFamily dao = new ExStaffFamily().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<ExStaffFamily> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from ex_staff_family  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

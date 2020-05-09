package com.github.qinyou.example.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.example.model.base.BaseExStaffEducation;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: ex_staff_education  员工教育经历
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings("serial")
public class ExStaffEducation extends BaseExStaffEducation<ExStaffEducation> {
    public static final ExStaffEducation dao = new ExStaffEducation().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<ExStaffEducation> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from ex_staff_education  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

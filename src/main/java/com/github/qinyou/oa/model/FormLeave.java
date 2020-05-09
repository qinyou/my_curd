package com.github.qinyou.oa.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.oa.model.base.BaseFormLeave;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: form_leave  请假流程表单
 *
 * @author zhangchuang
 * @since 2019-07-23 15:50:30
 */
@SuppressWarnings("serial")
public class FormLeave extends BaseFormLeave<FormLeave> {
    public static final FormLeave dao = new FormLeave().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<FormLeave> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from form_leave  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect += "order by createTime desc";
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

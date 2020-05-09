package com.github.qinyou.oa.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.oa.model.base.BaseBusinessFormInfo;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: business_form_info  业务表定义信息
 *
 * @author chuang
 * @since 2019-08-22 20:48:59
 */
@SuppressWarnings("serial")
public class BusinessFormInfo extends BaseBusinessFormInfo<BusinessFormInfo> {
    public static final BusinessFormInfo dao = new BusinessFormInfo().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<BusinessFormInfo> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from business_form_info  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

}

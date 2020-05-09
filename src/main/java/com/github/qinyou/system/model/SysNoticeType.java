package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysNoticeType;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: sys_notice_type  通知分类
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysNoticeType extends BaseSysNoticeType<SysNoticeType> {
    public static final SysNoticeType dao = new SysNoticeType().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysNoticeType> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_notice_type  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }


    /**
     * 根据编码查询
     *
     * @param code
     * @return
     */
    public SysNoticeType findByCode(String code) {
        return findFirst("select * from sys_notice_type where typeCode = ? ", code);
    }
}

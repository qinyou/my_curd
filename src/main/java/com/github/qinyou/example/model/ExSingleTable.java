package com.github.qinyou.example.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.example.model.base.BaseExSingleTable;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: ex_single_table  例子 单表结构
 *
 * @author zhangchuang
 * @since 2019-02-22 21:35:11
 */
@SuppressWarnings("serial")
public class ExSingleTable extends BaseExSingleTable<ExSingleTable> {
    public static final ExSingleTable dao = new ExSingleTable().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<ExSingleTable> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from ex_single_table  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }

    /**
     * 根据 where 条件查询
     *
     * @param where
     * @return
     */
    public List<ExSingleTable> findByWhere(String where) {
        String sql = " select * from ex_single_table ";
        if (StringUtils.notEmpty(where)) {
            sql += " where " + where;
        }
        return find(sql);
    }


    /**
     * 数量查询
     *
     * @param where
     * @return
     */
    public Long findCountByWhere(String where) {
        String sql = " select count(1) as c from ex_single_table ";
        if (StringUtils.notEmpty(where)) {
            sql += " where " + where;
        }
        return findFirst(sql).getLong("c");
    }
}

package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysButton;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: sys_button  菜单按钮
 *
 * @author zhangchuang
 * @since 2019-02-28 19:22:25
 */
@SuppressWarnings("serial")
public class SysButton extends BaseSysButton<SysButton> {
    public static final SysButton dao = new SysButton().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysButton> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_button  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);
    }


    public List<SysButton> findByProperty(String field, String value) {
        String sql = "select * from sys_button where " + field + " = ? ";
        return find(sql, value);
    }

    public SysButton findUniqueByProperty(String field, String value) {
        return findFirst("select * from sys_button where " + field + " = ?", value);
    }

}

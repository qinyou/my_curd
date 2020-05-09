package com.github.qinyou.system.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.base.BaseSysSetting;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Generated model
 * DB: sys_setting  系统设置项
 *
 * @author zhangchuang
 * @since 2019-06-15 20:24:55
 */
@SuppressWarnings("serial")
public class SysSetting extends BaseSysSetting<SysSetting> {
    public static final SysSetting dao = new SysSetting().dao();

    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<SysSetting> page(int pageNumber, int pageSize, String where) {
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from sys_setting  ";
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
    public List<SysSetting> findByWhere(String where) {
        String sql = " select * from sys_setting ";
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
        String sql = " select count(1) as c from sys_setting ";
        if (StringUtils.notEmpty(where)) {
            sql += " where " + where;
        }
        return findFirst(sql).getLong("c");
    }


    /**
     * 通过字段编码查询
     *
     * @param settingCode
     * @return
     */
    public SysSetting findBySettingCode(String settingCode) {
        return findFirst("select settingValue from sys_setting where settingCode = ?", settingCode);
    }
}

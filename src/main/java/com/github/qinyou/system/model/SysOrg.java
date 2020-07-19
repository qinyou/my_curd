package com.github.qinyou.system.model;

import com.github.qinyou.system.model.base.BaseSysOrg;

import java.util.List;

/**
 * Generated model
 * DB: sys_org  组织机构表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial","Duplicates"})
public class SysOrg extends BaseSysOrg<SysOrg> {
    public static final SysOrg dao = new SysOrg().dao();

    // 查询全部，根据排序号排序
    public List<SysOrg> findAllSort() {
        String sql = "select * from sys_org order by sortNum";
        return find(sql);
    }
}

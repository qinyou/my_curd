package com.github.qinyou.system.model;

import com.github.qinyou.system.model.base.BaseSysMenu;

import java.util.List;

/**
 * Generated model
 * DB: sys_menu  系统菜单
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings("serial")
public class SysMenu extends BaseSysMenu<SysMenu> {
    public static final SysMenu dao = new SysMenu().dao();

    // 查询全部数据 并根据排序号排序
    public List<SysMenu> findAllSort() {
        String sql = "select * from sys_menu order by sortNum,id";
        return find(sql);
    }

    // 查询全部数据（包含菜单按钮数量) 并根据排序号排序
    public List<SysMenu> findAllCSort() {
        String sql = "select a.*,(select count(1) from sys_button b where b.sysMenuId = a.id) as btnCount from sys_menu a order by a.sortNum,a.id";
        return find(sql);
    }
}

package com.github.qinyou.system.model;

import com.github.qinyou.system.model.base.BaseSysButton;

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

    public SysButton findUniqueByProperty(String field, String value) {
        return findFirst("select * from sys_button where " + field + " = ?", value);
    }

}

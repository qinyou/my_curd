package com.github.qinyou.system;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.system.controller.*;
import com.jfinal.config.Routes;

/**
 * System 模块路由配置
 *
 * @author zhangchuang
 */
public class SystemRoutes extends Routes {

    @Override
    public void config() {
        // 角色管理
        add("/sysRole", SysRoleController.class, Constant.VIEW_PATH);
        // 机构管理
        add("/sysOrg", SysOrgController.class, Constant.VIEW_PATH);
        // 菜单管理
        add("/sysMenu", SysMenuController.class, Constant.VIEW_PATH);
        // 用户管理
        add("/sysUser", SysUserController.class, Constant.VIEW_PATH);

        add("/sysDict", SysDictController.class, Constant.VIEW_PATH);

        // 访问日志
        add("/sysVisitLog", SysVisitLogController.class, Constant.VIEW_PATH);

        // 通知类型
        add("/sysNoticeType", SysNoticeTypeController.class, Constant.VIEW_PATH);

        // 用户解锁
        add("/sysUserUnlock", SysUserUnlockController.class, Constant.VIEW_PATH);

        // 定时任务
        add("/sysTask", SysTaskController.class, Constant.VIEW_PATH);

        // 系统设置
        add("/sysSetting", SysSettingController.class, Constant.VIEW_PATH);
    }
}

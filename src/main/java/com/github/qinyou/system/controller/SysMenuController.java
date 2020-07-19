package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.TreeTableUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysButton;
import com.github.qinyou.system.model.SysMenu;
import com.github.qinyou.system.model.SysRoleButton;
import com.github.qinyou.system.model.SysRoleMenu;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.*;

/**
 * 菜单管理
 */
@SuppressWarnings("Duplicates")
@RequirePermission("sysMenu")
public class SysMenuController extends BaseController {

    // 列表页
    public void index() {
        render("system/sysMenu.ftl");
    }
    // 表格数据
    public void query() {
        List<SysMenu> sysMenus = SysMenu.dao.findAllCSort();
        // easyui 字体图标
        for (SysMenu sysMenu : sysMenus) {
            sysMenu.put("iconCls", sysMenu.getIcon());
        }
        renderJson(sysMenus);
    }
    // 新增 、修改 弹窗
    public void newModel() {
        String id = get("id");
        if (StringUtils.notEmpty(id)) {
            SysMenu sysMenu = SysMenu.dao.findById(id);
            setAttr("sysMenu", sysMenu);
            if (sysMenu != null) {
                setAttr("pid", sysMenu.getPid());
            }
        } else {
            setAttr("pid", getPara("pid", "0"));
        }
        render("system/sysMenu_form.ftl");
    }
    // 添加 action
    public void addAction() {
        SysMenu sysMenu = getBean(SysMenu.class, "");
        sysMenu.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (sysMenu.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }
    // 修改 action
    public void updateAction() {
        SysMenu sysMenu = getBean(SysMenu.class, "");
        sysMenu.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (sysMenu.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }
    // 删除 action
    @Before(IdRequired.class)
    public void deleteAction() {
        String id = get("id");
        Db.tx(() -> {
            String allIds = TreeTableUtils.getSonTreeIds(id, "sys_menu", "id", "pid");
            if (StringUtils.isEmpty(allIds)) {
                return true;
            }
            allIds = allIds.replaceAll(",", "','");
            String sql = "delete from sys_menu where id in ('" + allIds + "')";
            Db.update(sql);
            sql = "delete from sys_role_menu where sysMenuId in ('" + allIds + "')";
            Db.update(sql);
            sql = "delete from sys_role_button where sysButtonId in (select id from sys_button where sysMenuId in ('" + allIds + "'))";
            Db.update(sql);
            sql = "delete from sys_button where sysMenuId in ('" + allIds + "')";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }
    // 新增 、编辑页下拉框数据
    public void menuComboTree() {
        List<SysMenu> sysMenus = SysMenu.dao.findAllSort();
        Set<String> pids = new HashSet<>();
        sysMenus.forEach(item -> pids.add(item.getPid()));
        List<Map<String, Object>> maps = new ArrayList<>();

        Map<String, Object> root = new HashMap<>();
        root.put("id", "0");
        root.put("pid", "-1");
        root.put("text", "根菜单");
        root.put("iconCls", "iconfont icon-root");
        root.put("state", sysMenus.size() > 0 ? "closed" : "open");
        maps.add(root);
        for (SysMenu sysMenu : sysMenus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sysMenu.getId());
            map.put("text", sysMenu.getMenuName());
            map.put("pid", sysMenu.getPid());
            map.put("iconCls", sysMenu.getIcon());
            if (pids.contains(sysMenu.getId())) {
                map.put("state", "closed");
            }
            maps.add(map);
        }
        renderJson(maps);
    }

    // 通过菜单查看相关角色
    public void roles() {
        setAttr("menuId", get("id"));
        render("system/sysMenu_role.ftl");
    }
    // 菜单相关角色数据
    @Before(SearchSql.class)
    public void queryRoles() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysRoleMenu> sysRoleMenuPage = SysRoleMenu.dao.pageRole(pageNumber, pageSize, where);
        renderDatagrid(sysRoleMenuPage);
    }


    // 菜单相关按钮页面
    public void buttons() {
        setAttr("menuId", get("id"));
        render("system/sysButton.ftl");
    }
    // 菜单相关按钮数据
    public void queryButtons() {
        String menuId = get("menuId");
        String sql = "select * from sys_button where sysMenuId = ? ";
        List<SysButton> sysButtons = SysButton.dao.find(sql, menuId);
        renderDatagrid(sysButtons, sysButtons.size());
    }
    // 新增、编辑按钮弹窗
    public void newButtonModel() {
        String id = get("id");
        String sysMenuId;
        if (StringUtils.notEmpty(id)) {
            SysButton sysButton = SysButton.dao.findById(id);
            setAttr("sysButton", sysButton);
            sysMenuId = sysButton.getSysMenuId();
        } else {
            sysMenuId = get("menuId");
        }
        setAttr("sysMenuId", sysMenuId);
        render("system/sysButton_form.ftl");
    }
    // 新增按钮 action
    @Before(Tx.class)
    public void addButtonAction() {
        SysButton sysButton = getBean(SysButton.class, "")
                .setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());

        if (SysButton.dao.findUniqueByProperty("buttonCode", sysButton.getButtonCode()) != null) {
            renderFail(ADD_FAIL + " 编码已经存在");
            return;
        }

        SysMenu sysMenu = SysMenu.dao.findById(sysButton.getSysMenuId());
        if (sysMenu == null) {
            renderFail(ADD_FAIL);
            return;
        }

        sysButton.save();
        renderSuccess(ADD_SUCCESS);
    }
    // 编辑按钮 action
    public void updateButtonAction() {
        SysButton sysButton = getBean(SysButton.class, "");
        sysButton.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());

        SysButton oldSysButton = SysButton.dao.findUniqueByProperty("buttonCode", sysButton.getButtonCode());
        if (oldSysButton != null && !sysButton.getId().equals(oldSysButton.getId())) {
            renderFail(ADD_FAIL + " 编码已经存在");
            return;
        }

        if (sysButton.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }
    // 删除按钮action
    @Before({Tx.class, IdsRequired.class})
    public void deleteButtonAction() {
        String sysMenuId = get("menuId");
        if (StringUtils.isEmpty(sysMenuId)) {
            renderFail("参数menuId 缺失");
            return;
        }
        String ids = get("ids");
        ids = ids.replaceAll(",", "','");
        String sql = "delete from sys_role_button where sysButtonId in ('" + ids + "')";
        Db.update(sql);
        sql = "delete from sys_button where id in ('" + ids + "')";
        Db.update(sql);

        renderSuccess(DELETE_SUCCESS);
    }

    // 按钮相关角色页面
    public void buttonRoles() {
        setAttr("buttonId", get("id"));
        render("system/sysButton_role.ftl");
    }
    // 按钮相关角色数据
    @Before(SearchSql.class)
    public void queryButtonRoles() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysRoleButton> sysRoleMenuPage = SysRoleButton.dao.pageRole(pageNumber, pageSize, where);
        renderDatagrid(sysRoleMenuPage);
    }
    //移除 按钮相关角色数据
    public void removeButtonRole() {
        String buttonId = get("buttonId");
        String roleId = get("roleId");
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(buttonId)) {
            renderFail("buttonId roleId 参数不可为空");
            return;
        }
        SysRoleButton.dao.deleteByIds(roleId, buttonId);
        renderSuccess("按钮角色删除成功");
    }
}

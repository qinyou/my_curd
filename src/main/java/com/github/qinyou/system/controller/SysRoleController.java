package com.github.qinyou.system.controller;

import com.github.qinyou.common.web.LoginService;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.*;
import com.google.common.base.Objects;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.*;

/**
 * 角色管理
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@RequirePermission("sysRole")
public class SysRoleController extends BaseController {

    /**
     * 首页
     */
    public void index() {
        render("system/sysRole.ftl");
    }


    /**
     * datagrid 数据
     */
    @SuppressWarnings("Duplicates")
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysRole> sysRolePage = SysRole.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysRolePage);
    }


    /**
     * 新增 或 修改弹窗
     */
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysRole sysRole = SysRole.dao.findById(id);
            setAttr("sysRole", sysRole);
        }
        render("system/sysRole_form.ftl");
    }


    /**
     * 新增 action
     */
    public void addAction() {
        SysRole sysRole = getBean(SysRole.class, "");
        sysRole.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (sysRole.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        SysRole sysRole = getBean(SysRole.class, "");
        sysRole.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (sysRole.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }


    /**
     * 删除 action
     */
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        Db.tx(() -> {
            // 删除角色数据
            String sql = "delete from sys_role where id in ('" + ids + "')";
            Db.update(sql);
            // 删除 角色用户 中间表
            sql = "delete from sys_user_role where sysRoleId in ('" + ids + "')";
            Db.update(sql);
            // 删除角色菜单中间表
            sql = "delete from sys_role_menu where sysRoleId in ('" + ids + "')";
            Db.update(sql);
            // 通知类型 角色 中间表
            sql = "delete from sys_notice_type_sys_role where sysRoleId in ('" + ids + "')";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }

    /**
     * 角色相关用户
     */
    public void openRoleUser() {
        setAttr("roleId", getPara("id"));
        render("system/sysRole_user.ftl");
    }

    /**
     * 角色相关用户数据
     */
    @Before(SearchSql.class)
    public void queryRoleUser() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysUserRole> sysUserRolePage = SysUserRole.dao.pageWithUserInfo(pageNumber, pageSize, where);
        renderDatagrid(sysUserRolePage);
    }

    /**
     * 角色相关用户 删除
     */
    public void deleteUserRole() {
        String userId = get("userId");
        String roleId = get("roleId");
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(userId)) {
            renderFail("userId roleId 参数不可为空");
            return;
        }
        SysUserRole.dao.deleteByIds(userId, roleId);
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 角色配置权限
     */
    public void openRolePermission() {
        setAttr("roleId", getPara("id"));
        render("system/sysRole_permission.ftl");
    }

    /**
     * 角色配置权限  菜单数据
     */
    public void menuTreeChecked() {
        String id = getPara("roleId");
        // 角色相关菜单
        List<SysRoleMenu> sysRoleMenus = SysRoleMenu.dao.findByRoleId(id);
        // 全部菜单
        List<SysMenu> sysMenus = SysMenu.dao.findAllSort();
        // 非叶子 菜单 id 集
        Set<String> pids = new HashSet<>();
        for (SysMenu sysMenu : sysMenus) {
            pids.add(sysMenu.getPid());
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sysMenu.getId());
            map.put("pid", sysMenu.getPid());
            map.put("text", sysMenu.getMenuName());
            map.put("iconCls", sysMenu.getIcon());

            // 非叶子折叠
//            if(pids.contains(sysMenu.getId())){
//                map.put("state", "closed");
//            }

            for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                // 中间表 有记录，且是 叶子 选中
                if (Objects.equal(sysRoleMenu.getSysMenuId(), sysMenu.getId()) && !pids.contains(sysMenu.getId())) {
                    map.put("checked", true);
                    break;
                }
            }
            maps.add(map);
        }
        renderJson(maps);
    }

    /**
     * 角色配置权限 菜单更新
     */
    @Before(Tx.class)
    public void menuTreeUpdate() {
        String roleId = get("roleId");
        String menuIds = get("menuIds");
        if (StringUtils.isEmpty(roleId)) {
            renderFail("roleId 参数不可为空.");
            return;
        }
        // 删除 角色原有菜单
        String deleteSql = "delete from  sys_role_menu where sysRoleId = ?";
        Db.update(deleteSql, roleId);

        // 添加 角色新菜单
        if (StringUtils.notEmpty(menuIds)) {
            String[] menuIdAry = menuIds.split(",");
            for (String menuId : menuIdAry) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setSysRoleId(roleId).setSysMenuId(menuId)
                        .setCreater(WebUtils.getSessionUsername(this))
                        .setCreateTime(new Date())
                        .save();
            }
        }
        renderSuccess("菜单权限操作成功");
    }

    /**
     * 通过 menuId 和  roleId 查询按钮列表
     */
    public void buttonList(){
      String menuId = getPara("menuId");
      String roleId = getPara("roleId");
      if(StringUtils.isEmpty(menuId) || StringUtils.isEmpty(roleId) ){
          renderJson(new ArrayList<>());
          return;
      }
      String sql = "SELECT a.id,a.buttonTxt,a.buttonCode,b.sysRoleId as checkFlag " +
              " FROM  sys_button a " +
              "	left join sys_role_button b on a.id = b.sysButtonId and b.sysRoleId = ? " +
              "WHERE " +
              "	sysMenuId = ? ";
      List<Record> btnList = Db.find(sql,roleId,menuId);
      renderJson(btnList);
    }


    // 关联 角色按钮
    public void buttonUpdate(){
        String roleId = getPara("roleId");
        String[] btnItems = getParaValues("btnItem");

        String deleteSql = "delete from  sys_role_button where sysRoleId = ?";
        Db.update(deleteSql, roleId);

        if(btnItems!=null){
            for (String buttonId : btnItems) {
                new SysRoleButton().setSysRoleId(roleId)
                        .setSysButtonId(buttonId)
                        .setCreater(WebUtils.getSessionUsername(this))
                        .setCreateTime(new Date())
                        .save();
            }
        }

        renderSuccess("按钮权限操作成功");
    }

}

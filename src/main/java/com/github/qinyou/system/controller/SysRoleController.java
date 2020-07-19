package com.github.qinyou.system.controller;

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

    // 列表页
    public void index() {
        render("system/sysRole.ftl");
    }
    // 表格数据
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysRole> sysRolePage = SysRole.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysRolePage);
    }
    // 新增、编辑弹窗
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysRole sysRole = SysRole.dao.findById(id);
            setAttr("sysRole", sysRole);
        }
        render("system/sysRole_form.ftl");
    }
    // 新增 action
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
    // 修改 action
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
    //  删除 action
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

    // 角色相关用户页面
    public void users() {
        setAttr("roleId", getPara("id"));
        render("system/sysRole_user.ftl");
    }
    // 角色相关用户 页面表格数据
    @Before(SearchSql.class)
    public void queryUsers() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysUserRole> sysUserRolePage = SysUserRole.dao.pageUser(pageNumber, pageSize, where);
        renderDatagrid(sysUserRolePage);
    }

    // 角色相关资源页面
    public void resources() {
        setAttr("roleId", getPara("id"));
        render("system/sysRole_resource.ftl");
    }
    // 角色相关菜单数据
    public void queryMenus() {
        String id = get("roleId");
        // 角色相关菜单
        List<SysRoleMenu> sysRoleMenus = SysRoleMenu.dao.findByRoleId(id);
        // 全部菜单
        List<SysMenu> sysMenus = SysMenu.dao.findAllCSort();
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
            map.put("btnCount",sysMenu.get("btnCount"));

            // 非叶子折叠
//            if(pids.contains(sysMenu.getId())){
//                map.put("state", "closed");
//            }

            for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                // 中间表 有记录，且是 叶子     选中
                if (Objects.equal(sysRoleMenu.getSysMenuId(), sysMenu.getId()) && !pids.contains(sysMenu.getId())) {
                    map.put("checked", true);
                    break;
                }
            }
            maps.add(map);
        }
        renderJson(maps);
    }
    // 更新角色 相关菜单
    @Before(Tx.class)
    public void updateMenus() {
        String roleId = get("roleId");
        String menuIds = get("menuIds");
        if (StringUtils.isEmpty(roleId)) {
            renderFail("roleId 参数不可为空.");
            return;
        }
        // 删除 角色原有菜单
        String sql = "delete from  sys_role_menu where sysRoleId = ?";
        Db.update(sql, roleId);

        // 防止保存 非叶子菜单（没有具体的controller)
        menuIds = menuIds.replace(",","','");
        sql = "select GROUP_CONCAT(id) as menuIds from sys_menu where url !='/' and id in ('"+menuIds+"')";
        Record record = Db.findFirst(sql);
        if(record==null || StringUtils.isEmpty(record.getStr("menuIds"))){
            renderFail("授权失败");
            return;
        }
        menuIds = record.getStr("menuIds");

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
        renderSuccess("授权成功");
    }
    // 角色相关按钮
    public void queryButtons(){
      String menuId = get("menuId");
      String roleId = get("roleId");
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
    // 角色 关联 按钮
    public void updateButtons(){
        String roleId = get("roleId");
        String sysMenuId = get("sysMenuId");
        String[] btnItems = getParaValues("btnItem");
        if(StringUtils.isEmpty(sysMenuId)){
            renderFail("授权失败，菜单参数不可为空");
            return;
        }
        // 删除角色  当前菜单下 按钮权限
        String deleteSql = "delete from  sys_role_button where sysRoleId = ? and sysButtonId in (select id from sys_button where sysMenuId = ?)";
        Db.update(deleteSql, roleId, sysMenuId);

        if(btnItems!=null){
            for (String buttonId : btnItems) {
                new SysRoleButton().setSysRoleId(roleId)
                        .setSysButtonId(buttonId)
                        .setCreater(WebUtils.getSessionUsername(this))
                        .setCreateTime(new Date())
                        .save();
            }
        }
        renderSuccess("授权成功");
    }

}

package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysUser;
import com.github.qinyou.system.model.SysUserRole;
import com.jfinal.aop.Before;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

@RequirePermission("sysUser")
public class SysUserController extends BaseController {
    private final String DEFAULT_PWD = "123456"; // 默认密码

    /**
     * 主页面
     */
    public void index() {
        render("system/sysUser.ftl");
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
        Page<SysUser> sysUserPage = SysUser.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysUserPage);
    }

    /**
     * 新增或者修改弹窗
     */
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysUser sysUser = SysUser.dao.findById(id);
            setAttr("sysUser", sysUser);
        }
        render("system/sysUser_form.ftl");
    }


    /**
     * add
     */
    public void addAction() {
        SysUser sysUser = getBean(SysUser.class, "");
        sysUser.setId(IdUtils.id()).setCreater(WebUtils.getSessionUsername(this)).setCreateTime(new Date()).setUserState("0");
        sysUser.setPassword(HashKit.sha1(DEFAULT_PWD));
        if (sysUser.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }

    }

    /**
     * update
     */
    public void updateAction() {
        SysUser sysUser = getBean(SysUser.class, "");
        sysUser.setUpdater(WebUtils.getSessionUsername(this)).setUpdateTime(new Date());
        if (sysUser.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }


    /**
     * delete
     */
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = get("ids").replaceAll(",", "','");
        Db.tx(() -> {
            // 修改删除标志
            String sql = "update sys_user set delFlag = 'X' where id in ('" + ids + "')";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 重置密码
     */
    @Before(IdsRequired.class)
    public void resetPwd() {
        String ids = getPara("ids").replaceAll(",", "','");
        String sha1Pwd = HashKit.sha1(DEFAULT_PWD);
        String sql = "update sys_user set password = ? where id in ('" + ids + "')";
        Db.update(sql, sha1Pwd);
        renderSuccess("重置密码成功。新密码: " + DEFAULT_PWD);
    }


    /**
     * 用户改角色弹窗
     */
    public void newUserRole() {
        setAttr("userId", getPara("id"));
        render("system/sysUser_role.ftl");
    }

    /**
     * 用户改角色数据
     */
    @Before(SearchSql.class)
    public void queryUserRole() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysUserRole> sysUserRolePage = SysUserRole.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysUserRolePage);
    }

    /**
     * 用户改角色保存
     */
    @SuppressWarnings("Duplicates")
    @Before(Tx.class)
    public void addUserRoleAction() {
        String userId = getPara("userId");
        String roleIds = getPara("roleIds");
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(roleIds)) {
            renderFail("userId roleIds 参数不可为空.");
            return;
        }
        String[] roleIdAry = roleIds.split(",");
        for (String roleId : roleIdAry) {
            SysUserRole sysUserRoleOld = SysUserRole.dao.findByIds(userId, roleId);
            if (sysUserRoleOld == null) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setSysUserId(userId).setSysRoleId(roleId)
                        .setCreater(WebUtils.getSessionUsername(this))
                        .setCreateTime(new Date())
                        .save();
            }
        }
        renderSuccess("添加用户角色成功.");
    }

    /**
     * 用户角色 删除
     */
    @Before(Tx.class)
    public void deleteUserRoleAction() {
        String userId = getPara("userId");
        String roleIds = getPara("roleIds");
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(roleIds)) {
            renderFail("userId roleIds 参数不可为空.");
            return;
        }
        String[] roleIdAry = roleIds.split(",");
        for (String roleId : roleIdAry) {
            SysUserRole.dao.deleteByIds(userId, roleId);
        }
        renderSuccess(DELETE_SUCCESS);
    }


}

package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysNoticeType;
import com.github.qinyou.system.model.SysNoticeTypeSysRole;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.Date;

@RequirePermission("sysNoticeType")
public class SysNoticeTypeController extends BaseController {
    public void index() {
        render("system/sysNoticeType.ftl");
    }


    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysNoticeType> sysNoticeTypePage = SysNoticeType.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysNoticeTypePage);
    }

    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysNoticeType sysNoticeType = SysNoticeType.dao.findById(id);
            setAttr("sysNoticeType", sysNoticeType);
        }
        render("system/sysNoticeType_form.ftl");
    }

    @SuppressWarnings("Duplicates")
    public void addAction() {
        SysNoticeType sysNoticeType = getBean(SysNoticeType.class, "");
        sysNoticeType.setId(IdUtils.id());
        sysNoticeType.setCreater(WebUtils.getSessionUsername(this)).setCreateTime(new Date());
        if (sysNoticeType.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }


    public void updateAction() {
        SysNoticeType sysNoticeType = getBean(SysNoticeType.class, "");
        sysNoticeType.setUpdater(WebUtils.getSessionUsername(this)).setUpdateTime(new Date());
        if (sysNoticeType.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }


    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        Db.tx(() -> {
            String sql = "delete from sys_notice_type where id in ('" + ids + "')";
            Db.update(sql);
            sql = "delete from sys_notice_type_sys_role where sysNoticeTypeId in ('" + ids + "')";
            Db.update(sql);
            // 删除通知消息表
            sql = "delete from sys_notice_detail where sysNoticeId in (select id from sys_notice where typeCode in (select typeCode from sys_notice_type where id in ('" + ids + "')))";
            Db.update(sql);
            sql = "delete from sys_notice where typeCode in (select typeCode from sys_notice_type where id in ('" + ids + "'))";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }

    /**
     * 通知类型配置角色弹窗
     */
    public void newTypeRole() {
        setAttr("typeId", get("id"));
        render("system/sysNoticeType_role.ftl");
    }

    /**
     * 通知类型 角色 datagrid 数据
     */
    @Before(SearchSql.class)
    public void queryTypeRole() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysNoticeTypeSysRole> sysNTRolePage = SysNoticeTypeSysRole.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysNTRolePage);
    }

    /**
     * 增加 关联关系
     */
    @Before(Tx.class)
    public void addTypeRoleAction() {
        String roleIds = getPara("roleIds");
        String sysNoticeTypeId = getPara("sysNoticeTypeId");
        SysNoticeTypeSysRole sysNTRoleOld;
        for (String roleId : roleIds.split(",")) {
            sysNTRoleOld = SysNoticeTypeSysRole.dao.findByIds(sysNoticeTypeId, roleId);
            if (sysNTRoleOld != null) {
                continue;
            }
            SysNoticeTypeSysRole sysNTRole = new SysNoticeTypeSysRole();
            sysNTRole.setSysNoticeTypeId(sysNoticeTypeId)
                    .setSysRoleId(roleId)
                    .setCreater(WebUtils.getSessionUsername(this))
                    .setCreateTime(new Date())
                    .save();
        }
        renderSuccess("关联角色成功");
    }

    /**
     * 单条 删除  关联关系
     */
    @Before(Tx.class)
    public void deleteTypeRoleAction() {
        // ,; 格式
        String idPairs = getPara("idPairs");
        if (StringUtils.isEmpty(idPairs)) {
            renderFail("参数不可为空");
            return;
        }
        String[] idPairAry = idPairs.split(";");
        String[] idAry;
        for (String idPair : idPairAry) {
            idAry = idPair.split(",");
            SysNoticeTypeSysRole.dao.deleteByIds(idAry[0], idAry[1]);
        }
        renderSuccess(DELETE_SUCCESS);
    }
}

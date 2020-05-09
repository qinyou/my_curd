package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.ComActionInterceptor;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysDict;
import com.github.qinyou.system.model.SysDictGroup;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

/**
 * 数据字典
 *
 * @author zhangchuang
 */
@RequirePermission("sysDict")
public class SysDictController extends BaseController {

    /**
     * 菜单页
     */
    public void index() {
        render("system/sysDict.ftl");
    }


    /**
     * sysDictGroup datagrid
     */
    @Before(SearchSql.class)
    public void queryGroup() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysDictGroup> sysDictGroupPage = SysDictGroup.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysDictGroupPage);
    }


    /**
     * 新增或编辑sysDictGroup 弹窗
     */
    public void newGroupModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysDictGroup sysDictGroup = SysDictGroup.dao.findById(id);
            setAttr("sysDictGroup", sysDictGroup);
        }
        render("system/sysDictGroup_form.ftl");
    }


    /**
     * 增加 sysDictGroup
     */
    @SuppressWarnings("Duplicates")
    public void addGroupAction() {
        SysDictGroup sysDictGroup = getBean(SysDictGroup.class, "");
        sysDictGroup.setId(IdUtils.id());
        sysDictGroup.setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (sysDictGroup.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }


    /**
     * 修改 sysDictGroup
     */
    public void updateGroupAction() {
        // 已存数据
        SysDictGroup sysDictGroupOld = SysDictGroup.dao.findById(get("id"));
        if (sysDictGroupOld == null) {
            renderSuccess(UPDATE_FAIL);
            return;
        }
        // 修改后的数据
        SysDictGroup sysDictGroup = getBean(SysDictGroup.class, "");
        sysDictGroup.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());

        if (!Objects.equal(sysDictGroup.getGroupCode(), sysDictGroupOld.getGroupCode())) {
            // 编码不一致
            List<SysDict> sysDictList = SysDict.dao.findAllByGroupCode(sysDictGroupOld.getGroupCode());
            if (sysDictList.size() > 0) {
                // 子表存在记录
                Db.tx(() -> {
                    String sql = "update sys_dict set groupCode = ? where groupCode = ?";
                    Db.update(sql, sysDictGroup.getGroupCode(), sysDictGroupOld.getGroupCode());
                    sysDictGroup.update();
                    return true;
                });
            } else {
                // 子表 不存在记录
                sysDictGroup.update();
            }
        } else {
            sysDictGroup.update();
        }
        renderSuccess(UPDATE_SUCCESS);
    }


    /**
     * 删除 sysDictGroup
     */
    @Before(IdsRequired.class)
    public void deleteGroupAction() {
        // 设置删除标志
        String ids = getPara("ids").replaceAll(",", "','");
        Db.tx(() -> {
            String sql = "update sys_dict set delFlag = 'X' where groupCode in (select groupCode from sys_dict_group where id in ('" + ids + "'))";
            Db.update(sql);
            sql = "update sys_dict_group set delFlag = 'X' where id in ('" + ids + "')";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * sysDict datagrid
     */
    @Before(SearchSql.class)
    public void queryDict() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysDict> sysDictPage = SysDict.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysDictPage);
    }


    /**
     * 新增或编辑 sysDict 弹窗
     */
    public void newDictModel() {
        String id = getPara("id");
        String groupCode;
        if (StringUtils.notEmpty(id)) {
            // 编辑页面
            SysDict sysDict = SysDict.dao.findById(id);
            setAttr("sysDict", sysDict);
            groupCode = sysDict.getGroupCode();
        } else {
            // 新增页面
            groupCode = getPara("groupCode");
        }
        Preconditions.checkNotNull(groupCode, "groupCode 参数不可为空");

        SysDictGroup sysDictGroup = SysDictGroup.dao.findByGroupCode(groupCode);
        if (sysDictGroup != null) {
            setAttr("groupName", sysDictGroup.getGroupName());
            setAttr("groupCode", sysDictGroup.getGroupCode());
        }
        render("system/sysDict_form.ftl");
    }


    /**
     * 增加 sysDict
     */
    public void addDictAction() {
        SysDict sysDict = getBean(SysDict.class, "");
        boolean flag = sysDict.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date())
                .save();
        if (flag) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }


    /**
     * 修改 sysDict
     */
    public void updateDictAction() {
        SysDict sysDict = getBean(SysDict.class, "");
        boolean flag = sysDict.setUpdater(WebUtils.getSessionUsername(this)).setUpdateTime(new Date()).update();
        if (flag) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }

    /**
     * 删除 sysDict
     */
    @Before(IdsRequired.class)
    public void deleteDictAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        String sql = "update sys_dict set delFlag = 'X' where id in ('" + ids + "')";
        Db.update(sql);
        renderSuccess(DELETE_SUCCESS);
    }


    @Clear({PermissionInterceptor.class, ComActionInterceptor.class})
    public void combobox() {
        String groupCode = get("groupCode", "");
        renderJson(SysDict.dao.findListByGroupAndState(groupCode, "on"));
    }
}


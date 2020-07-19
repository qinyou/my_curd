package com.github.qinyou.system.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.TreeTableUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysOrg;
import com.github.qinyou.system.model.SysUserOrg;
import com.google.common.base.Objects;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织机构管理
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@RequirePermission("sysOrg")
public class SysOrgController extends BaseController {

    public void index() {
        render("system/sysOrg.ftl");
    }

    /**
     * treegrid 查询数据
     */
    public void query() {
        List<SysOrg> sysOrgs = SysOrg.dao.findAllSort();
        Set<String> pids = new HashSet<>();
        sysOrgs.forEach(item -> pids.add(item.getPid()));

        for (SysOrg sysOrg : sysOrgs) {
            sysOrg.put("iconCls", "iconfont icon-org-tree");
            // 折叠 非根 的 分叶子节点
            if (!Objects.equal(sysOrg.getPid(), "0") && pids.contains(sysOrg.getId())) {
                sysOrg.put("state", "closed");
            }
        }
        renderJson(sysOrgs);
    }

    /**
     * 新增 或 修改 弹窗
     */
    public void newModel() {
        String id = get("id");
        if (StringUtils.notEmpty(id)) {
            SysOrg sysOrg = SysOrg.dao.findById(id);
            setAttr("sysOrg", sysOrg);
            if (sysOrg != null) {
                setAttr("pid", sysOrg.getPid());
            }
        } else {
            setAttr("pid", getPara("pid", "0"));
        }
        render("system/sysOrg_form.ftl");
    }

    /**
     * 添加 action
     */
    public void addAction() {
        SysOrg sysOrg = getBean(SysOrg.class, "");
        sysOrg.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (sysOrg.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        SysOrg sysOrg = getBean(SysOrg.class, "");
        sysOrg.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (sysOrg.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }


    /**
     * 删除 action
     */
    @Before(IdRequired.class)
    public void deleteAction() {
        String id = getPara("id");
        Db.tx(() -> {
            // 子孙id,包括自身
            String sonIds = TreeTableUtils.getSonTreeIds(id, "sys_org", "id", "pid");
            if (StringUtils.isEmpty(sonIds)) {
                return true;
            }
            sonIds = sonIds.replaceAll(",", "','");

            String sql = "delete from sys_org where id in ('" + sonIds + "')";
            Db.update(sql);

            sql = "delete from sys_user_org where sysOrgId in ('" + sonIds + "')";
            Db.update(sql);
            return true;
        });
        renderSuccess(DELETE_SUCCESS);
    }



    /**
     * 通过组织机构查询用户 datagrid 数据
     */
    @Before(SearchSql.class)
    public void queryUser() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);

        // 组织机构查询
        String orgId = getPara("extra_orgId");
        if (StringUtils.notEmpty(orgId)) {
            // 机构查询
            Boolean cascadeOrg = getParaToBoolean("extra_cascadeOrg", false);
            String whereSeg;
            if (cascadeOrg) {
                // 级联查询
                String sonIds = TreeTableUtils.getSonTreeIds(orgId, "sys_org", "id", "pid");
                if (StringUtils.notEmpty(sonIds)) {
                    sonIds = sonIds.replaceAll(",", "','");
                } else {
                    sonIds = "unknow";  // 查不到的
                }
                whereSeg = " b.sysOrgId in ('" + sonIds + "')";
            } else {
                whereSeg = " b.sysOrgId ='" + orgId + "' ";
            }
            if (StringUtils.isEmpty(where)) {
                where += whereSeg;
            } else {
                where += (" and " + whereSeg);
            }
        }
        Page<SysUserOrg> page = SysUserOrg.dao.pageUser(pageNumber,pageSize,where);
        renderDatagrid(page);
    }

}

package com.github.qinyou.common.web;

import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.ComActionInterceptor;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.system.model.*;
import com.google.common.base.Objects;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

/**
 * 工具controller
 */
@SuppressWarnings("Duplicates")
@Clear(PermissionInterceptor.class)
public class UtilsController extends BaseController {

    // 图像验证码
    public void captcha() {
        renderCaptcha();
    }

    // 二维码
    public void qrcode() {
        String content = getPara("txt", "my_curd very great");
        Integer width = getParaToInt("width", 200);
        Integer height = getParaToInt("height", 200);
        renderQrCode(content, width, height);
    }

    // 字典列表
    @Clear(ComActionInterceptor.class)
    public void dictList() {
        String groupCode = get("groupCode", "");
        renderJson(SysDict.dao.findListByGroupAndState(groupCode, "on"));
    }

    /**
     * 跳转到上传文件页面, 文件excel导入等 使用
     */
    public void goUploadFilePage() {
        String uploadUrl = get("uploadUrl");
        String label = get("label");
        if (StringUtils.isEmpty(uploadUrl)) {
            setAttr("msg", "uploadUrl参数不可为空");
            render("common/card.ftl");
            return;
        }
        setAttr("uploadUrl", uploadUrl);
        setAttr("label", label);
        render("common/utils/uploadFile.ftl");
    }

    // 用户信息弹窗
    public void userInfo() {
        String username = get("username");
        if (StringUtils.notEmpty(username)) {
            SysUser sysUser = SysUser.dao.findInfoByUsername(username);
            setAttr("sysUser", sysUser);
            List<Map<String,String>> orgs = WebUtils.userOrgs(username);
            setAttr("orgs",orgs);
        }
        setAttr("username", username);
        render("common/utils/userInfo.ftl");
    }

    // 机构弹窗
    public void orgInfo() {
        String orgId = getPara("id");
        if (StringUtils.notEmpty(orgId)) {
            SysOrg sysOrg = SysOrg.dao.findById(orgId);
            setAttr("sysOrg", sysOrg);
        }
        setAttr("orgId", orgId);
        render("common/utils/orgInfo.ftl");
    }

    // 组织机构 选择框 comboTree
    public void orgComboTree() {
        Boolean withRoot = getParaToBoolean("withRoot", true);
        List<SysOrg> sysOrgs = SysOrg.dao.findAll();
        // 非叶子id
        Set<String> pids = new HashSet<>();
        sysOrgs.forEach(item -> pids.add(item.getPid()));
        List<Map<String, Object>> maps = new ArrayList<>();
        // 编辑机构时需要
        if (withRoot) {
            Map<String, Object> root = new HashMap<>();
            root.put("id", "0");
            root.put("pid", "-1");
            root.put("text", "根机构");
            root.put("state", sysOrgs.size() > 0 ? "closed" : "open");
            root.put("iconCls", "iconfont icon-org-tree");
            maps.add(root);
        }
        for (SysOrg sysOrg : sysOrgs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sysOrg.getId());
            map.put("pid", sysOrg.getPid());
            map.put("text", sysOrg.getOrgName());
            map.put("iconCls", "iconfont icon-org-tree");
            if (pids.contains(sysOrg.getId())) {
                map.put("state", "closed");
            }
            maps.add(map);
        }
        renderJson(maps);
    }

    // 组织机构 tree 数据
    public void orgTreeData() {
        List<SysOrg> sysOrgs = SysOrg.dao.findAllSort();
        Set<String> pids = new HashSet<>();
        sysOrgs.forEach(item -> pids.add(item.getPid()));
        for (SysOrg sysOrg : sysOrgs) {
            sysOrg.put("iconCls", "iconfont icon-org-tree");
            if (!Objects.equal(sysOrg.getPid(), "0") && pids.contains(sysOrg.getId())) {
                sysOrg.put("state", "closed");
            }
        }
        renderJson(sysOrgs);
    }

    // 弹窗 角色选择
    public void role() {
        String singleSelect = getPara("singleSelect", "false");
        setAttr("singleSelect", singleSelect);
        setAttr("yesBtnTxt", getPara("yesBtnTxt", "添加角色"));
        render("common/utils/role.ftl");
    }
    @Before(SearchSql.class)
    public void queryRole() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysRole> sysRolePage = SysRole.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysRolePage);
    }

    // 弹窗选择用户
    public void user() {
        String singleSelect = getPara("singleSelect", "false");
        setAttr("singleSelect", singleSelect);
        setAttr("yesBtnTxt", getPara("yesBtnTxt", "添加用户"));
        render("common/utils/user.ftl");
    }
    @Before(SearchSql.class)
    public void queryUser() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysUser> sysUserPage = SysUser.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysUserPage);
    }


    /**
     * 通过角色 查询用户列表
     */
    public void userListByRole(){
        String roleCode = getPara("roleCode");
        if(StringUtils.isEmpty(roleCode)){
            renderFail("roleCode 参数 为空");
            return;
        }
        if(roleCode.startsWith("role_")){
            roleCode = roleCode.substring(5);
        }
        SysRole sysRole = SysRole.dao.findFirst("select * from sys_role where roleCode = ?",roleCode);
        setAttr("roleCode",roleCode);
        setAttr("roleName",sysRole.getRoleName());
        render("common/utils/userListByRole.ftl");
    }

    /**
     * 通过角色查询 用户列表
     */
    @Before(SearchSql.class)
    public void queryUserByRole(){
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysUserRole> page = SysUserRole.dao.pageUser(pageNumber,pageSize,where);
        renderDatagrid(page);
    }
}

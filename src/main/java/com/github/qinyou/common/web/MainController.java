package com.github.qinyou.common.web;

import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.ws.UserIdEncryptUtils;
import com.github.qinyou.system.model.*;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;

import java.util.*;


/**
 * 主页面
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@Clear(PermissionInterceptor.class)
public class MainController extends BaseController {

    public void index() {
        SysUser sysUser = WebUtils.getSysUser(this);
        setAttr("aesUserId", UserIdEncryptUtils.encrypt(sysUser.getId(), UserIdEncryptUtils.CURRENT_USER_ID_AESKEY));

        // 顶层菜单
        List<SysMenu> sysMenus = getSessionAttr("sysUserMenu");
        List<SysMenu> rootMenus = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            if("0".equals(sysMenu.getPid())){
                rootMenus.add(sysMenu);
            }
        });
        setAttr("rootMenus",rootMenus);

        // 根菜单id （默认取第一个根菜单）
        setAttr("rootMenuId",get(0,rootMenus.size()>0?rootMenus.get(0).getId():""));
        setAttr("orgName",getSessionAttr("orgName"));
        render("main.ftl");
    }


    /**
     * 树形菜单
     */
    public void menuTree() {
        String rootMenuId = getPara("rootMenuId");
        if(StringUtils.isEmpty(rootMenuId)){
            renderFail("rootMenuId 参数缺失");
            return;
        }

        // 拿出所有子菜单 并 排序
        List<SysMenu> sysMenus = getSessionAttr("sysUserMenu");
        Set<SysMenu> chainSet = new HashSet<>();
        MainService.getCChain(sysMenus, rootMenuId, chainSet);
        List<SysMenu> currentMenus  = new ArrayList<>(chainSet);
        currentMenus.sort((o1, o2) -> {
            if (o1.getSortNum() == null || o2.getSortNum() == null || o1.getSortNum() < o2.getSortNum()) {
                return -1;
            }
            return 0;
        });

        List<Map<String, Object>> maps = new ArrayList<>();
        for (SysMenu sysMenu : currentMenus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sysMenu.getId());
            map.put("pid", sysMenu.getPid());
            map.put("iconCls", sysMenu.getIcon());
            map.put("url", sysMenu.getUrl());
            map.put("text", sysMenu.getMenuName());
            maps.add(map);
        }
        renderJson(maps);
    }

    /**
     * 修改用户密码
     */
    public void userPass() {
        SysUser sysUser = WebUtils.getSysUser(this);
        setAttr("sysUser", sysUser);
        render("userPass.ftl");
    }
    public void changePwd() {
        String oldPwd = getPara("oldPwd");
        String newPwd = getPara("newPwd");
        String reNewPwd = getPara("reNewPwd");

        if (StringUtils.isEmpty(oldPwd)) {
            renderFail("请输入原密码");
            return;
        }
        if (StringUtils.isEmpty(newPwd)) {
            renderFail("请输入新密码");
            return;
        }
        if (!Objects.equals(newPwd, reNewPwd)) {
            renderFail("两次新密码不一致");
            return;
        }

        oldPwd = HashKit.sha1(oldPwd);
        SysUser curUser = WebUtils.getSysUser(this);
        SysUser sysUser = SysUser.dao.findByUsernameAndPassword(curUser.getUsername(), oldPwd);
        if (sysUser == null) {
            renderFail("旧密码错误");
            return;
        }
        if ("1".equals(sysUser.getUserState())) {
            renderFail("用户被禁用，无法修改密码");
            return;
        }

        newPwd = HashKit.sha1(newPwd);
        sysUser.setPassword(newPwd);
        boolean updateFlag = sysUser.update();

        if (updateFlag) {
            renderSuccess("修改密码成功");
        } else {
            renderFail("修改密码失败");
        }
    }


    /**
     * 修改用户信息
     */
    public void userInfo() {
        SysUser sysUser = WebUtils.getSysUser(this);
        setAttr("sysUser", SysUser.dao.findInfoByUsername(sysUser.getUsername()));
        List<Map<String,String>> orgs = WebUtils.userOrgs(sysUser.getUsername());
        setAttr("orgs",orgs);
        render("userInfo.ftl");
    }

    public void changeUserInfo() {
        String id = getPara("userId");
        if (StringUtils.isEmpty(id)) {
            renderFail("参数错误");
            return;
        }
        SysUser sysUser = SysUser.dao.findById(id);
        if (sysUser == null) {
            renderFail("参数错误");
            return;
        }

        String avatar = getPara("avatar");
        if (StringUtils.notEmpty(avatar)) {
            sysUser.setAvatar(avatar);
        }

        String phone = getPara("phone");
        if (StringUtils.notEmpty(phone)) {
            sysUser.setPhone(phone);
        }

        String email = getPara("email");
        if (StringUtils.notEmpty(email)) {
            sysUser.setEmail(email);
        }

        String realName = getPara("realName");
        if (StringUtils.notEmpty(realName)) {
            sysUser.setRealName(realName);
        }

        String job = getPara("job");
        if (StringUtils.notEmpty(job)) {
            sysUser.setJob(job);
        }

        String gender = getPara("gender");
        if (StringUtils.notEmpty(gender)) {
            sysUser.setGender(gender);
        }

        if (sysUser.update()) {
            SysUser curUser = WebUtils.getSysUser(this);
            curUser.setRealName(sysUser.getRealName());
            curUser.setJob(sysUser.getJob());
            curUser.setGender(sysUser.getGender());
            curUser.setEmail(sysUser.getEmail());
            curUser.setPhone(sysUser.getPhone());
            curUser.setAvatar(sysUser.getAvatar());
            renderSuccess("信息修改成功");
        } else {
            renderFail("信息修改失败");
        }
    }


    // 用户切换结构
    public void userOrgs(){
        String username = WebUtils.getSessionUsername(this);
        List<Map<String,String>> orgs = WebUtils.userOrgs(username);
        setAttr("username",username);
        setAttr("orgs",orgs);
        setAttr("currentOrg",getSessionAttr("orgId"));
        render("userOrgs.ftl");
    }

    public void changeUserOrg(){
        String orgId = get("org");
        SysOrg sysOrg = SysOrg.dao.findById(orgId);
        if(orgId==null){
            renderFail("Org 参数错误");
            return;
        }
        // 检验参数是否合法
        SysUser sysUser = WebUtils.getSysUser(this);
        String sql = "select count(1) as c from sys_user_org where sysUserId = ? and sysOrgId = ?";
        if(Db.findFirst(sql,sysUser.getId(),orgId).getLong("c")== 0L){
            renderFail("非法操作");
            return;
        }
        String orgName = WebUtils.buildOrgName(sysOrg.getOrgName(),sysOrg.getPid());
        setSessionAttr("orgId",orgId);
        setSessionAttr("orgName",orgName);
        renderSuccess("切换成功");
    }


    /**
     * 用户通知
     */
    public void userNotice() {
        render("userNotice.ftl");
    }

    /**
     * 用户通知数据
     */
    @Before(SearchSql.class)
    public void noticeData() {
        String userId = WebUtils.getSysUser(this).getId();
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);

        if (StringUtils.notEmpty(where)) {
            where += " and receiver = '" + userId + "'";
        } else {
            where = " receiver = '" + userId + "'";
        }
        Page<SysNotice> sysNoticePage = SysNotice.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysNoticePage);
    }

    /**
     * 当前用户 单条系统通知 设置为已读
     */
    public void noticeSetRead() {
        String detailId = getPara("detailId");
        if (StringUtils.isEmpty(detailId)) {
            renderFail("detailId 参数不可为空");
            return;
        }
        SysNoticeDetail sysNoticeDetail = SysNoticeDetail.dao.findById(detailId);
        if (sysNoticeDetail == null) {
            renderFail("数据不存在");
            return;
        }
        String userId = WebUtils.getSysUser(this).getId();
        if (!userId.equals(sysNoticeDetail.getReceiver())) {
            renderFail("禁止查看他人数据");
            return;
        }
        if ("N".equals(sysNoticeDetail.getHasRead())) {
            sysNoticeDetail.setHasRead("Y");
            sysNoticeDetail.setReadTime(new Date());
            sysNoticeDetail.update();
        }
        renderSuccess("操作成功");
    }

    /**
     * 当前用户通知全部设置为已读
     */
    public void noticeSetAllRead() {
        SysUser sysUser = WebUtils.getSysUser(this);
        SqlPara sqlPara = new SqlPara();
        String sql = "update sys_notice_detail set hasRead = 'Y' , readTime = ? where receiver = ? and hasRead = 'N' ";
        sqlPara.setSql(sql).addPara(new Date()).addPara(sysUser.getId());
        Db.update(sqlPara);
        renderSuccess("设置 全部已读 操作成功");
    }

    /**
     * 获得 未读消息数量
     */
    public void noticeUnreadCount() {
        SysUser sysUser = WebUtils.getSysUser(this);
        String sql = " select count(1) as unreadCount from sys_notice_detail where receiver = ? and hasRead !='Y' ";
        Record record = Db.findFirst(sql, sysUser.getId());
        Ret ret = Ret.create().setOk().set("unreadCount", record == null ? 0 : record.get("unreadCount"));
        renderJson(ret);
    }
}

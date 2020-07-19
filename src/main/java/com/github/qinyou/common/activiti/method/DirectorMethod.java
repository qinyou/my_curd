package com.github.qinyou.common.activiti.method;

import com.github.qinyou.system.model.SysOrg;
import com.github.qinyou.system.model.SysUser;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 获取员工部门主管 方法
 */
@Slf4j
public class DirectorMethod implements Serializable {
    private final String GROUP_DIRECTOR = "DIRECTOR"; // 部门主管组编码
    private final String DEFAULT_DIRECTOR = "admin";  // 默认负责人用户名

    /**
     * 获取 用户 部门负责人
     * 1. 普通员工，找寻当前机构负责人，如查找不到，找上级部门负责人, 直到找到为止
     * 2. 负责人，找寻上级部门负责人，找到为止
     * 3. 如果一直查询不到负责人，返回默认负责人 为用户 admin
     *
     * @param username
     * @return 负责人用户名
     */
    public String findDirector(String username) {
        log.info("username: {}", username);

        Optional<SysUser> optional = Optional.of(SysUser.dao.findByUsername(username));
        // 正常情况 用户只取第一个
        String sql = "select sysOrgId from sys_user_org where sysUserId = ?";
        List<Record> list = Db.find(sql, optional.get().getId());
        if (list.size() == 0) {
            log.info("SET_DEFAULT_ASSIGNEE. 用户 {} 查找不到机构信息", username);
            log.info("DEFAULT_ASSIGNEE: {}", DEFAULT_DIRECTOR);
            return DEFAULT_DIRECTOR;
        }

        // 取第一个, 没有明确的组织机构
        String orgId = list.get(0).getStr("sysOrgId");
        boolean flag = isDirector(username);
        if (flag) {
            // 当前用户为 部门主管，从上级机构开始查询
            SysOrg org = SysOrg.dao.findById(orgId);
            if ("0".equals(org.getPid())) {
                log.info("SET_DEFAULT_ASSIGNEE. 用户{}为部门主管，当前机构{}为顶层机构", username, orgId);
                log.info("DEFAULT_ASSIGNEE: {}", DEFAULT_DIRECTOR);
                return DEFAULT_DIRECTOR;
            }
            orgId = org.getPid();
        }

        String ret = getDirectorByOrgId(orgId);
        return ret;
    }

    /**
     * 判断 某用户是否为 部门主管(DIRECTOR) 分组
     *
     * @param username
     * @return
     */
    private boolean isDirector(String username) {
        boolean flag = false;
        String sql = "select count(1) as c from sys_user a, sys_role b, sys_user_role c " +
                " where a.id = c.sysUserId and b.id = c.sysRoleId and a.username = ? and b.roleCode = ?";
        if (Db.findFirst(sql, username, GROUP_DIRECTOR).getLong("c") > 0L) {
            flag = true;
        }
        return flag;
    }

    /**
     * 递归查询部门主管
     *
     * @param orgId 组织机构id
     * @return 部门主管 用户名, 如查询不到，默认admin
     */
    private String getDirectorByOrgId(String orgId) {
        String sql = "select a.username as username from sys_user a, sys_org b, sys_user_org c , sys_role d, sys_user_role e " +
                " where  c.sysUserId = a.id and c.sysOrgId = b.id " +
                " and e.sysUserId = a.id and e.sysRoleId = d.id " +
                " and d.roleCode = ? and b.id = ?";
        List<Record> list = Db.find(sql, GROUP_DIRECTOR, orgId);
        if (list.size() > 0) {
            return list.get(0).getStr("username");
        } else {
            SysOrg org = SysOrg.dao.findById(orgId);
            if ("0".equals(org.getPid())) {
                log.info("SET_DEFAULT_ASSIGNEE. 机构{}为顶层机构，且机构下找不到部门主管", orgId);
                log.info("DEFAULT_ASSIGNEE: {}", DEFAULT_DIRECTOR);
                return DEFAULT_DIRECTOR;
            }
            return getDirectorByOrgId(org.getPid());
        }
    }
}

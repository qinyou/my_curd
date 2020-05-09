package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_user  系统用户表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysUser<M extends BaseSysUser<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 用户民
    public String getUsername() {
        return getStr("username");
    }

    public M setUsername(String username) {
        set("username", username);
        return (M) this;
    }


    // 密码
    public String getPassword() {
        return getStr("password");
    }

    public M setPassword(String password) {
        set("password", password);
        return (M) this;
    }


    // 姓名
    public String getRealName() {
        return getStr("realName");
    }

    public M setRealName(String realName) {
        set("realName", realName);
        return (M) this;
    }


    // 头像
    public String getAvatar() {
        return getStr("avatar");
    }

    public M setAvatar(String avatar) {
        set("avatar", avatar);
        return (M) this;
    }


    // 性别M男F女
    public String getGender() {
        return getStr("gender");
    }

    public M setGender(String gender) {
        set("gender", gender);
        return (M) this;
    }


    // 电子邮箱
    public String getEmail() {
        return getStr("email");
    }

    public M setEmail(String email) {
        set("email", email);
        return (M) this;
    }


    // 电话
    public String getPhone() {
        return getStr("phone");
    }

    public M setPhone(String phone) {
        set("phone", phone);
        return (M) this;
    }


    // 部门
    public String getOrgId() {
        return getStr("orgId");
    }

    public M setOrgId(String orgId) {
        set("orgId", orgId);
        return (M) this;
    }


    // 职位
    public String getJob() {
        return getStr("job");
    }

    public M setJob(String job) {
        set("job", job);
        return (M) this;
    }


    // 职位级别
    public String getJobLevel() {
        return getStr("jobLevel");
    }

    public M setJobLevel(String jobLevel) {
        set("jobLevel", jobLevel);
        return (M) this;
    }


    // 微信预留
    public String getWx() {
        return getStr("wx");
    }

    public M setWx(String wx) {
        set("wx", wx);
        return (M) this;
    }


    // 钉钉预留
    public String getDd() {
        return getStr("dd");
    }

    public M setDd(String dd) {
        set("dd", dd);
        return (M) this;
    }


    // 最后登录时间
    public Date getLastLoginTime() {
        return get("lastLoginTime");
    }

    public M setLastLoginTime(Date lastLoginTime) {
        set("lastLoginTime", lastLoginTime);
        return (M) this;
    }


    // 创建人
    public String getCreater() {
        return getStr("creater");
    }

    public M setCreater(String creater) {
        set("creater", creater);
        return (M) this;
    }


    // 创建时间
    public Date getCreateTime() {
        return get("createTime");
    }

    public M setCreateTime(Date createTime) {
        set("createTime", createTime);
        return (M) this;
    }


    // 最后修改人
    public String getUpdater() {
        return getStr("updater");
    }

    public M setUpdater(String updater) {
        set("updater", updater);
        return (M) this;
    }


    // 最后修改时间
    public Date getUpdateTime() {
        return get("updateTime");
    }

    public M setUpdateTime(Date updateTime) {
        set("updateTime", updateTime);
        return (M) this;
    }


    // 用户状态 0正常1禁用
    public String getUserState() {
        return getStr("userState");
    }

    public M setUserState(String userState) {
        set("userState", userState);
        return (M) this;
    }


    // 删除标志  X 已删除
    public String getDelFlag() {
        return getStr("delFlag");
    }

    public M setDelFlag(String delFlag) {
        set("delFlag", delFlag);
        return (M) this;
    }
}

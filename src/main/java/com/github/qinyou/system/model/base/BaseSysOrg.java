package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_org  组织机构表
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysOrg<M extends BaseSysOrg<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 名称
    public String getOrgName() {
        return getStr("orgName");
    }

    public M setOrgName(String orgName) {
        set("orgName", orgName);
        return (M) this;
    }


    // 编码
    public String getOrgCode() {
        return getStr("orgCode");
    }

    public M setOrgCode(String orgCode) {
        set("orgCode", orgCode);
        return (M) this;
    }


    // 地址
    public String getAddress() {
        return getStr("address");
    }

    public M setAddress(String address) {
        set("address", address);
        return (M) this;
    }


    // 备注
    public String getRemark() {
        return getStr("remark");
    }

    public M setRemark(String remark) {
        set("remark", remark);
        return (M) this;
    }


    // 排序号
    public Integer getSortNum() {
        return getInt("sortNum");
    }

    public M setSortNum(Integer sortNum) {
        set("sortNum", sortNum);
        return (M) this;
    }


    // 父ID
    public String getPid() {
        return getStr("pid");
    }

    public M setPid(String pid) {
        set("pid", pid);
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
}

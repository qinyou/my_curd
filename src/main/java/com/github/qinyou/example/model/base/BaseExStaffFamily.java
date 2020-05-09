package com.github.qinyou.example.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: ex_staff_family  员工家人
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseExStaffFamily<M extends BaseExStaffFamily<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 姓名
    public String getName() {
        return getStr("name");
    }

    public M setName(String name) {
        set("name", name);
        return (M) this;
    }


    // 关系
    public String getRelation() {
        return getStr("relation");
    }

    public M setRelation(String relation) {
        set("relation", relation);
        return (M) this;
    }


    // 主键id
    public String getExStaffId() {
        return getStr("exStaffId");
    }

    public M setExStaffId(String exStaffId) {
        set("exStaffId", exStaffId);
        return (M) this;
    }


    // 工作
    public String getJob() {
        return getStr("job");
    }

    public M setJob(String job) {
        set("job", job);
        return (M) this;
    }


    // 联系方式
    public String getPhone() {
        return getStr("phone");
    }

    public M setPhone(String phone) {
        set("phone", phone);
        return (M) this;
    }


    // 添加人
    public String getCreater() {
        return getStr("creater");
    }

    public M setCreater(String creater) {
        set("creater", creater);
        return (M) this;
    }


    // 添加时间
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

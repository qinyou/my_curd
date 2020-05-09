package com.github.qinyou.oa.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: form_leave  请假流程表单
 *
 * @author zhangchuang
 * @since 2019-07-23 15:50:30
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseFormLeave<M extends BaseFormLeave<M>> extends Model<M> implements IBean {


    // 主键
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 请假类型
    public String getLeaveType() {
        return getStr("leaveType");
    }

    public M setLeaveType(String leaveType) {
        set("leaveType", leaveType);
        return (M) this;
    }


    // 删除标志字段
    public String getDelFlag() {
        return getStr("delFlag");
    }

    public M setDelFlag(String delFlag) {
        set("delFlag", delFlag);
        return (M) this;
    }


    // 开始时间
    public Date getStartTime() {
        return get("startTime");
    }

    public M setStartTime(Date startTime) {
        set("startTime", startTime);
        return (M) this;
    }


    // 结束时间
    public Date getEndTime() {
        return get("endTime");
    }

    public M setEndTime(Date endTime) {
        set("endTime", endTime);
        return (M) this;
    }


    // 请假原因
    public String getLeaveReason() {
        return getStr("leaveReason");
    }

    public M setLeaveReason(String leaveReason) {
        set("leaveReason", leaveReason);
        return (M) this;
    }


    // 创建人用户名
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


    // 最后修改人用户名
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

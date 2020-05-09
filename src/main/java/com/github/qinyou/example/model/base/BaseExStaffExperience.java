package com.github.qinyou.example.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: ex_staff_experience  员工工作经历
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseExStaffExperience<M extends BaseExStaffExperience<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 公司名
    public String getCompanyName() {
        return getStr("companyName");
    }

    public M setCompanyName(String companyName) {
        set("companyName", companyName);
        return (M) this;
    }


    // 行业
    public String getTrade() {
        return getStr("trade");
    }

    public M setTrade(String trade) {
        set("trade", trade);
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


    // 主键id
    public String getExStaffId() {
        return getStr("exStaffId");
    }

    public M setExStaffId(String exStaffId) {
        set("exStaffId", exStaffId);
        return (M) this;
    }


    // 证明人
    public String getWitness() {
        return getStr("witness");
    }

    public M setWitness(String witness) {
        set("witness", witness);
        return (M) this;
    }


    // 证明人联系方式
    public String getWitnessPhone() {
        return getStr("witnessPhone");
    }

    public M setWitnessPhone(String witnessPhone) {
        set("witnessPhone", witnessPhone);
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

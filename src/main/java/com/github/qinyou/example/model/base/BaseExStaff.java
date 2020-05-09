package com.github.qinyou.example.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: ex_staff  一线员工
 *
 * @author zhangchuang
 * @since 2019-02-22 22:13:44
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseExStaff<M extends BaseExStaff<M>> extends Model<M> implements IBean {


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


    // 性别
    public String getGender() {
        return getStr("gender");
    }

    public M setGender(String gender) {
        set("gender", gender);
        return (M) this;
    }


    // 身份证号
    public String getIdCard() {
        return getStr("idCard");
    }

    public M setIdCard(String idCard) {
        set("idCard", idCard);
        return (M) this;
    }


    // 民族
    public String getNation() {
        return getStr("nation");
    }

    public M setNation(String nation) {
        set("nation", nation);
        return (M) this;
    }


    // 身高CM
    public Integer getHeight() {
        return getInt("height");
    }

    public M setHeight(Integer height) {
        set("height", height);
        return (M) this;
    }


    // 体重KG
    public Integer getWeight() {
        return getInt("weight");
    }

    public M setWeight(Integer weight) {
        set("weight", weight);
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


    // 家庭地址
    public String getHomeAddress() {
        return getStr("homeAddress");
    }

    public M setHomeAddress(String homeAddress) {
        set("homeAddress", homeAddress);
        return (M) this;
    }


    // 手机号
    public String getPhoneNumber() {
        return getStr("phoneNumber");
    }

    public M setPhoneNumber(String phoneNumber) {
        set("phoneNumber", phoneNumber);
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

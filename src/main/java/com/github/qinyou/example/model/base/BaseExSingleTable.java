package com.github.qinyou.example.model.base;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;
import java.util.StringJoiner;

/**
 * Generated baseModel
 * DB table: ex_single_table  例子 单表结构
 *
 * @author zhangchuang
 * @since 2019-02-22 21:35:11
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseExSingleTable<M extends BaseExSingleTable<M>> extends Model<M> implements IBean {
    // --- 导出导出excel 所需-----
    @Excel(name = "姓名", height = 10, width = 30)
    private String name;
    @Excel(name = "年龄", height = 10, width = 30)
    private Integer age;
    @Excel(name = "性别", height = 10, width = 30)
    private String gender;
    @Excel(name = "添加人", height = 10, width = 30)
    private String creater;
    @Excel(name = "添加时间", importFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy-MM-dd HH:mm:ss", height = 10, width = 30)
    private Date createTime;
    @Excel(name = "最后修改人", height = 10, width = 30)
    private String updater;
    @Excel(name = "最后修改时间", importFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy-MM-dd HH:mm:ss", height = 10, width = 30)
    private Date updateTime;
    //--- 导出导出excel 所需-----


    // 主键
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


    // 年龄
    public Integer getAge() {
        return getInt("age");
    }

    public M setAge(Integer age) {
        set("age", age);
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

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseExSingleTable.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("age=" + getAge())
                .add("gender='" + getGender() + "'")
                .add("creater='" + getCreater() + "'")
                .add("createTime=" + getCreateTime())
                .add("updater='" + getUpdater() + "'")
                .add("updateTime=" + getUpdateTime())
                .toString();
    }
}

package com.github.qinyou.system.model.base;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_setting  系统设置项
 *
 * @author zhangchuang
 * @since 2019-06-15 20:24:55
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysSetting<M extends BaseSysSetting<M>> extends Model<M> implements IBean {
    // --- 导出导出excel 所需-----
    @Excel(name = "说明", height = 10, width = 30)
    private String settingInfo;
    @Excel(name = "设置编码", height = 10, width = 30)
    private String settingCode;
    @Excel(name = "设置编码值", height = 10, width = 30)
    private String settingValue;
    @Excel(name = "最后修改人", height = 10, width = 30)
    private String updater;
    @Excel(name = "最后修改时间", height = 10, width = 30)
    private Date updateTime;
    //--- 导出导出excel 所需-----


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 说明
    public String getSettingInfo() {
        return getStr("settingInfo");
    }

    public M setSettingInfo(String settingInfo) {
        set("settingInfo", settingInfo);
        return (M) this;
    }


    // 设置编码
    public String getSettingCode() {
        return getStr("settingCode");
    }

    public M setSettingCode(String settingCode) {
        set("settingCode", settingCode);
        return (M) this;
    }


    // 设置编码值
    public String getSettingValue() {
        return getStr("settingValue");
    }

    public M setSettingValue(String settingValue) {
        set("settingValue", settingValue);
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

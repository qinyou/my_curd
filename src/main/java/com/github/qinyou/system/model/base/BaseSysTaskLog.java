package com.github.qinyou.system.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

import java.util.Date;

/**
 * Generated baseModel
 * DB table: sys_task_log  定时任务日志
 *
 * @author zhangchuang
 * @since 2019-02-21 13:10:49
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysTaskLog<M extends BaseSysTaskLog<M>> extends Model<M> implements IBean {


    // 主键id
    public String getId() {
        return getStr("id");
    }

    public M setId(String id) {
        set("id", id);
        return (M) this;
    }


    // 定时任务类名
    public String getClassName() {
        return getStr("className");
    }

    public M setClassName(String className) {
        set("className", className);
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


    // 运行结果，success 或者 fail
    public String getResult() {
        return getStr("result");
    }

    public M setResult(String result) {
        set("result", result);
        return (M) this;
    }


    // 异常信息
    public String getError() {
        return getStr("error");
    }

    public M setError(String error) {
        set("error", error);
        return (M) this;
    }
}

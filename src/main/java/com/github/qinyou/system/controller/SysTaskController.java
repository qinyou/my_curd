package com.github.qinyou.system.controller;

import com.github.qinyou.AppConfig;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.ReflectionUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysTaskLog;
import com.github.qinyou.system.vo.Cron4jTaskIntro;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.jfinal.aop.Before;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.cron4j.ITask;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 定时任务
 */
@RequirePermission("sysTask")
@Slf4j
public class SysTaskController extends BaseController {

    private static void initData(List<Cron4jTaskIntro> data) {
        String taskNames = AppConfig.configProp.get("cron4j");
        if (StringUtils.isEmpty(taskNames)) {
            return;
        }
        String[] taskNameAry = taskNames.split(",");
        for (String taskName : taskNameAry) {
            String cron = AppConfig.configProp.get(taskName + ".cron");
            String className = AppConfig.configProp.get(taskName + ".class");
            String daemon = AppConfig.configProp.get(taskName + ".daemon");
            String enable = AppConfig.configProp.get(taskName + ".enable");
            Cron4jTaskIntro intro = new Cron4jTaskIntro();

            intro.setName(taskName);
            intro.setCron(cron);
            intro.setClassName(className);
            intro.setDaemon(daemon);
            intro.setEnable(enable);

            data.add(intro);
        }
    }

    public void index() {
        render("system/sysTask.ftl");
    }

    public void query() {
        List<Cron4jTaskIntro> data = new ArrayList<>();
        initData(data);

        // 查询条件
        String name = getPara("extra_name");
        String className = getPara("extra_class");
        String daemon = getPara("extra_daemon");
        String enable = getPara("extra_enable");

        Collection<Cron4jTaskIntro> result = null;

        // Cron4jTaskIntro 字段 均不为 null
        if (StringUtils.notEmpty(name)) {
            result = Collections2.filter(data, x -> x.getName().contains(name));
        }
        if (StringUtils.notEmpty(className)) {
            result = Collections2.filter(data, x -> x.getClassName().contains(className));
        }
        if (StringUtils.notEmpty(daemon)) {
            result = Collections2.filter(data, x -> x.getDaemon().equals(daemon));
        }
        if (StringUtils.notEmpty(enable)) {
            result = Collections2.filter(data, x -> x.getEnable().contains(enable));
        }

        if (result == null) {
            renderDatagrid(data, data.size());
        } else {
            renderDatagrid(result, result.size());
        }
    }

    /**
     * 运行一次  (同步)
     */
    public void runOnce() {
        String className = getPara("className");
        if (StringUtils.isEmpty(className)) {
            renderFail("className 参数不可为空");
            return;
        }

        // 每一次url 访问 都会重新 创建 controller 对象，忽然感觉 jfinal 很恐怖  :(,
        // 此处只好如此加锁
        synchronized (SysTaskController.class) {
            try {
                // 通过反射运行
                Class classObject = Class.forName(className);
                Object instance = classObject.newInstance();
                if (instance instanceof ITask) {
                    ReflectionUtils.runMethod(instance, "run");
                    ReflectionUtils.runMethod(instance, "stop");
                } else if (instance instanceof Runnable) {
                    ReflectionUtils.runMethod(instance, "run");
                } else {
                    renderFail("定时任务只支持 ITask 和 Runable");
                    return;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                renderFail("运行异常，原因:" + Throwables.getStackTraceAsString(e));
                return;
            }
        }

        renderSuccess("运行成功");
    }

    /**
     * 运行日志
     */
    public void taskLog() {
        setAttr("className", getPara("className"));
        render("system/sysTaskLog.ftl");
    }

    @Before(SearchSql.class)
    public void taskLogQuery() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysTaskLog> sysTaskLogPage = SysTaskLog.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysTaskLogPage);
    }

    @Before(IdsRequired.class)
    public void deleteTaskLogAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        String sql = "delete from sys_task_log where id in ('" + ids + "')";
        Db.update(sql);
        renderSuccess(DELETE_SUCCESS);
    }
}

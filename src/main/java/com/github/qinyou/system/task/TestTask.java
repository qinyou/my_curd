package com.github.qinyou.system.task;

import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.SysTaskLog;
import com.jfinal.plugin.cron4j.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 示例 定时任务, 非业务需要
 * 需要 5s 执行时间 的定时任务
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
public class TestTask implements ITask {

    private final static Logger LOG = LoggerFactory.getLogger(TestTask.class);

    @Override
    public void stop() {
        LOG.debug("task stop");
    }

    @Override
    public void run() {
        // 样板式 代码
        SysTaskLog sysTaskLog = new SysTaskLog();
        sysTaskLog.setId(IdUtils.id());
        sysTaskLog.setClassName(this.getClass().getName());
        sysTaskLog.setStartTime(new Date());
        String errMsg = null;

        // 业务代码
        try {
            LOG.debug("task start");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.debug("task end");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            errMsg = e.getMessage();
        }

        // 样板式 代码
        sysTaskLog.setEndTime(new Date());
        if (StringUtils.isEmpty(errMsg)) {
            sysTaskLog.setResult("success");
        } else {
            sysTaskLog.setResult("fail");
            sysTaskLog.setError(errMsg);
        }
        sysTaskLog.save();
    }
}

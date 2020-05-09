package com.github.qinyou.system.task;

import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.model.SysTaskLog;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 删除系统通知，删除 "必死期" 主从表记录，删除 过期未读 从表记录
 * @author chuang
 */
@SuppressWarnings("Duplicates")
public class DelSysNoticeTask implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(DelSysNoticeTask.class);

    @Override
    public void run() {
        // 样板 代码
        SysTaskLog sysTaskLog = new SysTaskLog();
        sysTaskLog.setId(IdUtils.id());
        sysTaskLog.setClassName(this.getClass().getName());
        sysTaskLog.setStartTime(new Date());
        String errMsg = null;

        // 业务
        Date today = new Date();
        LOG.debug(" 定时任务执行。 (删除 “过期未读” 和 “必死” 的 系统通知数据表 数据）");
        try {
            Db.tx(() -> {
                // 删除 “必死期” 的 主从表记录
                String selectSql = "select group_concat(id) as ids from sys_notice  where deadTime <= ? ";
                String deleteSql;
                String ids;
                Record record = Db.findFirst(selectSql, today);
                if (StringUtils.notEmpty(record.getStr("ids"))) {
                    ids = record.getStr("ids").replaceAll(",", "','");
                    deleteSql = " delete from  sys_notice where id in ('" + ids + "')";
                    Db.update(deleteSql);
                    deleteSql = " delete from  sys_notice_detail where sysNoticeId in ('" + ids + "')";
                    Db.update(deleteSql);
                }

                // 删除 过期 未读的从表记录
                selectSql = " select group_concat(id) as ids from sys_notice where expiryTime <= ? ";
                record = Db.findFirst(selectSql, today);
                if (StringUtils.notEmpty(record.getStr("ids"))) {
                    ids = record.getStr("ids").replaceAll(",", "','");
                    deleteSql = " delete from  sys_notice_detail where sysNoticeId in ('" + ids + "') and hasRead='N' ";
                    Db.update(deleteSql);
                }
                return true;
            });
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            errMsg = e.getMessage();

        }


        // 样板 代码
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

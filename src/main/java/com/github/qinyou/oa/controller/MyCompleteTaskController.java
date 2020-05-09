package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.ProcessInstanceInfo;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 已办任务
 */
@RequirePermission("myCompleteTask")
public class MyCompleteTaskController extends BaseController {
    public void index() {
        render("oa/myCompleteTask.ftl");
    }

    public void query() {

        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);
        String processName = get("extra_processName");
        String applyUser = get("extra_applyUser");
        String instanceId = getPara("extra_instanceId");

        String username = WebUtils.getSessionUsername(this);
        String sqlSelect = "SELECT " +
                "	c.TEXT_ AS initiator, " +
                "	a.PROC_INST_ID_ AS processInstanceId, " +
                "	b.NAME_ AS name, " +
                "	b.START_TIME_ AS startTime, " +
                "	b.END_TIME_ AS endTime, " +
                "   b.BUSINESS_KEY_ as businessKey  ";
        String sqlExceptSelect = "FROM " +
                "	( SELECT PROC_INST_ID_ FROM act_hi_taskinst WHERE ASSIGNEE_ = '" + username + "' AND END_TIME_ IS NOT NULL GROUP BY PROC_INST_ID_ ) a " +
                "	LEFT JOIN act_hi_procinst b ON a.PROC_INST_ID_ = b.ID_ " +
                "	LEFT JOIN act_hi_varinst c ON a.PROC_INST_ID_ = c.PROC_INST_ID_  AND c.NAME_ = 'initiator'  " +
                "   where b.DELETE_REASON_ is null   and b.START_USER_ID_ != '" + username + "' ";  // 已办任务 不查询自己申请的流程
        // and c.TEXT_ != '"+username+"'  加上此句，已办任务 不查询 自己申请的

        if (StringUtils.notEmpty(processName)) {
            sqlExceptSelect = sqlExceptSelect + " and b.NAME_ like '%" + processName + "%'";
        }
        if (StringUtils.notEmpty(applyUser)) {
            sqlExceptSelect = sqlExceptSelect + " and c.TEXT_ like '%" + applyUser + "%'";
        }
        if (StringUtils.notEmpty(instanceId)) {
            sqlExceptSelect = sqlExceptSelect + " and a.PROC_INST_ID_ =  '" + instanceId + "'";
        }
        sqlExceptSelect = sqlExceptSelect + " order by b.START_TIME_ desc ";

        Page<Record> page = Db.use(ActivitiConfig.DATASOURCE_NAME).paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);


        List<ProcessInstanceInfo> retList = new ArrayList<>();
        for (Record record : page.getList()) {
            ProcessInstanceInfo info = new ProcessInstanceInfo();
            info.setProcessInstanceId(record.getStr("processInstanceId"));
            info.setStartTime(record.getDate("startTime"));
            info.setEndTime(record.getDate("endTime"));
            info.setName(record.getStr("name"));
            info.setInitiator(record.getStr("initiator"));
            if (info.getEndTime() == null) {
                info.setActivityName(ActivitiKit.getActivityName(record.getStr("businessKey")));
            }
            retList.add(info);
        }
        renderDatagrid(retList, page.getTotalRow());
    }
}

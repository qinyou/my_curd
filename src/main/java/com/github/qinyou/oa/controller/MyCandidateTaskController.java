package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.TaskInfo;
import com.google.common.base.Joiner;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的 代办候选任务
 *
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("myCandidateTask")
public class MyCandidateTaskController extends BaseController {

    public void index() {
        render("oa/myCandidateTask.ftl");
    }

    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);

        List<String> roleCodeList = getSessionAttr("roleCodes");
        List<String> roleCodeListNew = new ArrayList<>();
        for (String roleCode : roleCodeList) {
            roleCodeListNew.add("role_" + roleCode);
        }
        String roleCodes = Joiner.on("','").join(roleCodeListNew);
        String username = WebUtils.getSessionUsername(this);
        String sqlSelect = " SELECT DISTINCT RES.* ";
        String sqlExceptSelect = "  FROM " +
                "	ACT_RU_TASK RES " +
                "	INNER JOIN ACT_RU_IDENTITYLINK I ON I.TASK_ID_ = RES.ID_ " +
                "	INNER JOIN ACT_RU_IDENTITYLINK I_OR0 ON I_OR0.TASK_ID_ = RES.ID_  " +
                "WHERE " +
                "	RES.SUSPENSION_STATE_ = 1  " +
                "	AND ( " +
                "		( RES.ASSIGNEE_ IS NULL AND I.TYPE_ = 'candidate' AND ( I.GROUP_ID_ IN ( '" + roleCodes + "' ) ) ) " +
                "	    OR  " +
                "       ( RES.ASSIGNEE_ IS NULL AND I_OR0.TYPE_ = 'candidate' AND ( I_OR0.USER_ID_ = '" + username + "' ) ) " +
                "	) ";

        String instanceId = getPara("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            sqlExceptSelect += " and RES.PROC_INST_ID_ = '" + instanceId + "' ";
        }
        String taskName = getPara("extra_taskName");
        if (StringUtils.notEmpty(taskName)) {
            sqlExceptSelect += " and RES.NAME_ like '%" + taskName + "%' ";
        }
        sqlExceptSelect += " ORDER BY RES.CREATE_TIME_ desc ";

        Page<Record> page = Db.use(ActivitiConfig.DATASOURCE_NAME).paginate(pageNumber, pageSize, sqlSelect, sqlExceptSelect);

        List<TaskInfo> list = new ArrayList<>();
        ProcessInstance processInstance;
        for (Record record : page.getList()) {
            processInstance = ActivitiKit.getRuntimeProcessInstance(record.getStr("PROC_INST_ID_"), true);
            TaskInfo taskInfo = new TaskInfo()
                    .setId(record.getStr("ID_"))
                    .setCreateTime(record.getDate("CREATE_TIME_"))
                    .setTaskName(record.getStr("NAME_"))
                    .setTaskDefinitionKey(record.getStr("TASK_DEF_KEY_"))
                    .setProcessInstanceName(processInstance.getName())
                    .setProcessInstanceId(record.getStr("PROC_INST_ID_"))
                    .setInitiator((String) processInstance.getProcessVariables().get("initiator"));
            list.add(taskInfo);
        }
        renderDatagrid(list, page.getTotalRow());
    }

    /**
     * 认领任务
     */
    @Before(IdRequired.class)
    public void claimAction() {
        String taskId = getPara("id");

        Task task = ActivitiKit.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            renderFail("任务不存在");
            return;
        }
        if (task.isSuspended()) {
            renderFail("任务已被暂停");
            return;
        }
        if (task.getAssignee() != null) {
            renderFail("任务已被认领");
            return;
        }

        ActivitiKit.getTaskService().claim(taskId, WebUtils.getSessionUsername(this));
        renderSuccess("认领成功");
    }

}

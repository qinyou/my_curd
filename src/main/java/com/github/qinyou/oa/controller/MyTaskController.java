package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.TaskInfo;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户代办任务
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("myTask")
public class MyTaskController extends BaseController {

    public void index() {
        render("oa/myTask.ftl");
    }

    /**
     * 查询用户任务
     */
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);
        String username = WebUtils.getSessionUsername(this);
        TaskQuery query = ActivitiKit.getTaskService().createTaskQuery();
        query.taskAssignee(username).active();

        String instanceId = getPara("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }
        String taskName = getPara("extra_taskName");
        if (StringUtils.notEmpty(taskName)) {
            query.taskNameLike("%" + taskName + "%");
        }

        Long total = query.count();
        List<Task> tasks = query.orderByTaskCreateTime().desc().listPage((pageNumber - 1) * pageSize, pageSize);

        List<TaskInfo> list = new ArrayList<>();
        ProcessInstance processInstance;
        for (Task task : tasks) {
            processInstance = ActivitiKit.getRuntimeProcessInstance(task.getProcessInstanceId(), true);
            TaskInfo taskInfo = new TaskInfo()
                    .setId(task.getId())
                    .setCreateTime(task.getCreateTime())
                    .setTaskName(task.getName())
                    .setTaskDefinitionKey(task.getTaskDefinitionKey())
                    .setProcessInstanceName(processInstance.getName())
                    .setProcessInstanceId(task.getProcessInstanceId())
                    .setInitiator((String) processInstance.getProcessVariables().get("initiator"));

            list.add(taskInfo);
        }

        renderDatagrid(list, total.intValue());
    }

    /**
     * 跳转到任务办理界面
     */
    @Before(IdRequired.class)
    public void goCompleteForm() {
        String taskId = getPara("id");
        Task task = ActivitiKit.getTaskService().createTaskQuery()
                .taskId(taskId).singleResult();

        set("taskName", task.getName());
        if (StringUtils.notEmpty(task.getDescription())) {
            set("taskDescription", task.getDescription());
        }
        set("taskId", task.getId());
        String processInstanceId = task.getProcessInstanceId();
        set("processInstanceId", processInstanceId);

        ProcessInstance processInstance = ActivitiKit.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();

        String businessForm = (String) processInstance.getProcessVariables().get("businessForm");
        String initiator = (String) processInstance.getProcessVariables().get("initiator");
        String renderedTaskForm = (String) ActivitiKit.getFormService().getRenderedTaskForm(taskId);

        setAttr("initiator", initiator);  // 发起人
        setAttr("processInstanceName", processInstance.getName()); // 流程实例名
        setAttr("businessKey", processInstance.getBusinessKey()); // 业务表 id
        setAttr("businessForm", businessForm);  // 业务表名
        setAttr("renderedTaskForm", renderedTaskForm); // 任务表单

        // 如果是调整表单, 显示调整操作
        String taskDefinitionKey = task.getTaskDefinitionKey();
        setAttr("taskDefinitionKey", taskDefinitionKey);
        if ("adjustForm".equals(taskDefinitionKey)) {
            String adjustFormUrl = StrKit.toCamelCase(businessForm) + "/newModel?id=" + getAttr("businessKey");
            setAttr("adjustFormUrl", adjustFormUrl);
        }

        render("oa/myTask_complete.ftl");
    }

    /**
     * 完成任务
     */
    @TxConfig(ActivitiConfig.DATASOURCE_NAME)
    @Before({IdRequired.class, Tx.class})
    public void completeAction() {
        String taskId = getPara("id");
        String processInstanceId = getPara("processInstanceId");
        if (StringUtils.isEmpty(processInstanceId)) {
            renderFail("processInstanceId 参数为空");
            return;
        }
        Task task = ActivitiKit.getTaskService().createTaskQuery()
                .taskId(taskId)
                .singleResult();
        Map<String, Object> formParams = ActivitiKit.getFormParams(this);

        String comment = getPara("comment", "");
        if (task.getFormKey() != null) {
            // 有表单
            // 特殊表单格式 name fp_en-中文
            Map<String, Object> variables = new LinkedHashMap<>();
            formParams.forEach((k, v) -> {
                if (k.contains("-")) {
                    String[] nameAry = k.split("-");
                    String kTemp, vTemp;
                    kTemp = nameAry[0];
                    vTemp = nameAry[1] + ": " + v;
                    variables.put(kTemp, v);
                    ActivitiKit.getTaskService().addComment(taskId, processInstanceId, vTemp);
                } else {
                    variables.put(k, v);
                    ActivitiKit.getTaskService().addComment(taskId, processInstanceId, v.toString());
                }
            });
            ActivitiKit.getTaskService().addComment(taskId, processInstanceId, comment);
            ActivitiKit.getTaskService().complete(taskId, variables);
        } else {
            // 无表单
            ActivitiKit.getTaskService().addComment(taskId, processInstanceId, comment);
            ActivitiKit.getTaskService().complete(taskId);
        }

        renderSuccess("办理成功");
    }


    // 转办
    @RequirePermission("myTask:changeAssignee")
    @TxConfig(ActivitiConfig.DATASOURCE_NAME)
    @Before(Tx.class)
    public void changeAssigneeAction() {
        String taskId = getPara("taskId"); // taskId;
        String username = getPara("username");
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(username)) {
            renderFail("参数缺失");
            return;
        }
        TaskService taskService = ActivitiKit.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            renderFail("任务不存在");
            return;
        }
        if (username.equals(task.getAssignee())) {
            renderFail("原任务处理人 和 转办人 不可相同");
            return;
        }

        // 设置上个任务处理人
        ActivitiKit.getTaskService().setVariableLocal(taskId, "lastAssignee", task.getAssignee());
        ActivitiKit.getTaskService().setAssignee(taskId, username);
        renderSuccess("转办 操作成功 ");
    }


}

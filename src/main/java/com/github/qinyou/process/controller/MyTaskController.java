package com.github.qinyou.process.controller;

import com.alibaba.fastjson.JSON;
import com.github.qinyou.common.activiti.ActConst;
import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.process.model.ActFormTpl;
import com.github.qinyou.system.model.SysUser;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户待办任务
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("myTask")
public class MyTaskController extends BaseController {
    public void index() {
        render("process/myTask.ftl");
    }

    /**
     * 查询用户任务
     */
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        TaskQuery query = ActivitiUtils.getTaskService().createTaskQuery()
                .taskAssignee(WebUtils.getSessionUsername(this))
                .active();

        String instanceId = get("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }
        String taskName = get("extra_taskName");
        if (StringUtils.notEmpty(taskName)) {
            query.taskNameLike("%" + taskName + "%");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByTaskCreateTime().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize)
                .forEach(task -> {
                    Map<String, Object> taskInfo = new HashMap<>();
                    taskInfo.put("id", task.getId()); // 任务id
                    taskInfo.put("taskName", task.getName());
                    taskInfo.put("createTime", task.getCreateTime());
                    taskInfo.put("instanceId", task.getProcessInstanceId());
                    ProcessInstance processInstance = ActivitiUtils.getRuntimeProcessInstance(task.getProcessInstanceId(), false);
                    taskInfo.put("instanceName", processInstance.getName());
                    list.add(taskInfo);
                });
        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }

    /**
     * 跳转到任务办理界面
     */
    public void goCompleteForm() {
        String taskId = get("taskId");
        if (StringUtils.isEmpty(taskId)) {
            renderFail("taskId参数不可为空");
            return;
        }
        Task task = ActivitiUtils.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            log.info("taskId:{} 任务不存在", taskId);
            renderFail("参数错误");
            return;
        }

        set("adjustAble", task.getTaskDefinitionKey().equalsIgnoreCase(ActConst.ADJUST_APPLY_TASK_KEY));// 能否调整表单
        set("taskId", task.getId());
        set("taskName", task.getName());
        set("taskKey",task.getTaskDefinitionKey());
        if (StringUtils.notEmpty(task.getDescription())) {
            set("taskDescription", task.getDescription());
        }

        String renderedTaskForm = ActivitiUtils.getRenderedTaskFrom(task);
        set("renderedTaskForm", renderedTaskForm); // 任务表单

        String instanceId = task.getProcessInstanceId();
        ProcessInstance instance = ActivitiUtils.getRuntimeProcessInstance(instanceId,true);
        set("instanceId", instanceId);                        // 实例id
        set("instanceName", instance.getName());              // 实例名称
        set("initiator", (String) instance.getProcessVariables().get(ActConst.APPLY_USER));   // 发起人
        set("applyFormData", (String) instance.getProcessVariables().get( ActConst.APPLY_FORM_DATA));// 申请表单数据

        String formTplId = (String) instance.getProcessVariables().get( ActConst.APPLY_FORM_TPL_ID);
        ActFormTpl formTpl = ActFormTpl.dao.findById(formTplId);
        set("applyFormTpl", formTpl.getTemplate());            // 申请表单模板
        set("applyFormTplPlugins", formTpl.getPlugins());      // 申请表单插件

        render("process/myTask_complete.ftl");
    }

    /**
     * 完成任务
     */
    @Before(Tx.class)
    public void completeAction() {
        String taskId = get("taskId");
        String instanceId = get("instanceId");
        if (StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(taskId)) {
            renderFail("参数缺失");
            return;
        }
        TaskService service = ActivitiUtils.getTaskService();
        Task task = service.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            log.info("taskId:{} 任务不存在", taskId);
            renderFail("参数错误");
            return;
        }
        if (task.getAssignee() == null) {
            renderFail("未认领任务无法办理");
            return;
        }
        String username = WebUtils.getSessionUsername(this);
        if (!username.equals(task.getAssignee())) {
            log.info("username: {} 尝试办理 无权限任务 taskId: {}", username, taskId);
            renderFail("无权办理");
            return;
        }

        // 意见
        String comment = get("comment");
        if (StringUtils.notEmpty(comment)) {
            service.addComment(taskId, null, comment);
        }

        // 附件
        String[] attachments = getParaValues("attachments");
        if (attachments != null && attachments.length > 0) {
            String[] temp;
            for (String fileUri : attachments) {
                temp = fileUri.split("#SEP#"); // 文件名#SEP#文件地址
                service.createAttachment("file", taskId, null, temp[0], null, temp[1]);
            }
        }

        // 审批表单数据
        Map<String, String> formParams = ActivitiUtils.getFormParams(this, taskId);
        log.info("params: {}",formParams.toString());
        Map<String, Object> variables = new HashMap<>();
        String scope,eKey,cKey; // 作用范围，英文变量名，中文变量名
        for (String key : formParams.keySet()) {
            String[] temp = key.split("_");  // 长度为3
            if(temp.length != 3){
                throw new RuntimeException("任务表单ID:"+key+"配置错误");
            }
            scope = temp[0];
            eKey = temp[1];
            cKey = temp[2];
            switch (scope){
                case ActConst.PROCESS_VAR_PREFIX:
                    // 实例 和 任务 范围 都添加)
                    service.setVariableLocal(taskId, cKey, formParams.get(key));
                    variables.put(eKey, formParams.get(key));
                    System.out.println(eKey+"---"+formParams.get(key));
                    break;
                case ActConst.TASK_VAR_PREFIX:
                    // 添加 到 任务范围
                    service.setVariableLocal(taskId, cKey, formParams.get(key));
                    break;
                case ActConst.APPLY_VAR_PREFIX:
                    service.setVariableLocal(taskId, cKey, formParams.get(key));
                    ProcessInstance instance = ActivitiUtils.getRuntimeProcessInstance(task.getProcessInstanceId(),true);
                    String applyFormData = (String) instance.getProcessVariables().get(ActConst.APPLY_FORM_DATA);
                    Map<String, Object> applyForm = JSON.parseObject(applyFormData, Map.class);
                    applyForm.put(eKey, formParams.get(key));
                    variables.put(ActConst.APPLY_FORM_DATA, JSON.toJSONString(applyForm));
                    break;
                default:
                    log.info("无效表单参数: {}:{}", key, formParams.get(key));
            }
        }
        service.complete(taskId, variables);
        renderSuccess("办理完成");
    }


    // 调整申请表单
    @Before(Tx.class)
    public void adjustApply() {
        String taskId = get("taskId");
        if (StringUtils.isEmpty(taskId)) {
            renderFail("参数缺失");
            return;
        }
        TaskService service = ActivitiUtils.getTaskService();
        Task task = service.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            renderFail("参数错误");
            return;
        }
        String taskDefinitionKey = task.getTaskDefinitionKey();
        if (!ActConst.ADJUST_APPLY_TASK_KEY.equals(taskDefinitionKey)) {
            renderFail("当前节点禁止调整申请");
            return;
        }
        String applyFormData = get("applyFormData");
        service.setVariable(taskId, ActConst.APPLY_FORM_DATA, applyFormData);// 设置流程实例 变量值
        renderSuccess("调整申请成功");
    }


    // 转办
    @RequirePermission("myTask:changeAssignee")
    public void changeAssigneeAction() {
        String taskId = get("taskId");
        String username = get("username");
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(username)) {
            renderFail("参数缺失");
            return;
        }
        TaskService taskService = ActivitiUtils.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            log.info("taskId:{} 任务不存在", taskId);
            renderFail("参数错误");
            return;
        }
        if (task.getAssignee() == null) {
            renderFail("未认领任务无法转办");
            return;
        }
        String currentUser = WebUtils.getSessionUsername(this);
        if (!currentUser.equals(task.getAssignee())) {
            log.info("username: {} 尝试转办 无权限任务 taskId: {}", username, taskId);
            renderFail("无权办理");
            return;
        }
        if (username.equals(task.getAssignee())) {
            renderFail("新办理人 不能和 原办理人相同");
            return;
        }
        SysUser sysUser = SysUser.dao.findByUsername(username);
        if (sysUser == null) {
            renderFail("新办理人无效");
            return;
        }

        // TODO 任务被多次转办 log历史信息
        ActivitiUtils.getTaskService().setVariableLocal(taskId, "lastAssignee", task.getAssignee());
        ActivitiUtils.getTaskService().setAssignee(taskId, username);
        renderSuccess("转办完成");
    }
}

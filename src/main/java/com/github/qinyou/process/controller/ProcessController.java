package com.github.qinyou.process.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.qinyou.common.activiti.ActConst;
import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.activiti.DiagramUtils;
import com.github.qinyou.common.interceptor.PermissionInterceptor;
import com.github.qinyou.common.render.ImageRender;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.process.model.ActFormTpl;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * process 模块 公共 controller
 */
@SuppressWarnings("Duplicates")
@Slf4j
@Clear(PermissionInterceptor.class)
public class ProcessController extends BaseController {

    // 流程定义图
    @Before(IdRequired.class)
    public void definitionDiagram() {
        // id 为 流程定义id
        render(new ImageRender().inputStream(ActivitiUtils.getRepositoryService().getProcessDiagram(get("id"))));
    }

    // 流程跟踪高亮图
    @Before(IdRequired.class)
    public void instanceDiagram() {
        try {
            render(new ImageRender().bytes( ActivitiUtils.getInstanceDiagram(get("id")))); //  流程实例id
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            renderFail("系统异常");
        }
    }

    // 流程当前节点信息
    @Before(IdRequired.class)
    public void instanceCurrent(){
        renderSuccess(ActivitiUtils.getCurrent( get("id"))); //流程实例id
    }

    // 流程详情
    @Before(IdRequired.class)
    public void processInstanceDetail() {
        String processInstanceId = get("id"); // 流程实例 id
        HistoricProcessInstance instance = ActivitiUtils.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .includeProcessVariables()
                .singleResult();
        String initiator = (String) instance.getProcessVariables().get(ActConst.APPLY_USER); // 申请人
        String applyFormData = (String) instance.getProcessVariables().get(ActConst.APPLY_FORM_DATA); // 发起表单数据
        String formTplId = (String) instance.getProcessVariables().get(ActConst.APPLY_FORM_TPL_ID);// 发起表单模板id
        ActFormTpl formTpl = ActFormTpl.dao.findById(formTplId);
        set("initiator", initiator);
        set("instanceId", processInstanceId);
        set("instanceName", instance.getName());
        set("applyFormData", applyFormData);
        set("applyFormTpl", formTpl.getTemplate()); // 表单模板
        if(instance.getDeleteReason()!=null){
            set("delReason",instance.getDeleteReason()); // 删除原因（用户取消任务才会存在）
        }
        if(instance.getEndTime()!=null){
            set("endTime",instance.getEndTime()); // 流程结束时间
        }
        render("process/processInstance_detail.ftl");
    }

    // 流程运行节点信息
    @Before(IdRequired.class)
    public void historicTaskInstances() {
        String processInstanceId = get("id"); // 流程实例id
        set("instanceId", processInstanceId);
        List<HistoricTaskInstance> historicTaskInstances = ActivitiUtils.getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime().asc()
                .includeTaskLocalVariables()
                .list();
        List<Map<String, Object>> historicTaskInfos = new ArrayList<>();
        historicTaskInstances.forEach(historicTaskInstance -> {
            Map<String, Object> info = new HashMap<>();
            // 1. 放入基础信息
            info.put("id", historicTaskInstance.getId()); // 任务id
            info.put("name", historicTaskInstance.getName()); // 任务名
            info.put("startTime", historicTaskInstance.getStartTime());  // 开始时间
            info.put("endTime", historicTaskInstance.getEndTime());      //结束时间
            info.put("claimTime", historicTaskInstance.getClaimTime());  // 认领时间

            // 2. 放入任务处理人、候选人、候选组信息
            // 如果任务为转办任务，此变量保存上一次 转办人
            String lastAssignee = (String) historicTaskInstance.getTaskLocalVariables().get("lastAssignee");
            if (StringUtils.notEmpty(lastAssignee)) {
                info.put("lastAssignee", lastAssignee);
            }
            if (StringUtils.notEmpty(historicTaskInstance.getAssignee())) {
                // 如果任务有处理人，放入处理人
                info.put("assignee", historicTaskInstance.getAssignee());
            } else {
                // 没有任务处理人，放入候选组或候选用户
                List<HistoricIdentityLink> historicIdentityLinks = ActivitiUtils.getHistoryService().getHistoricIdentityLinksForTask(historicTaskInstance.getId());
                List<String> candidateGroup = new ArrayList<>();
                List<String> candidateUser = new ArrayList<>();
                historicIdentityLinks.forEach(historicIdentityLink -> {
                    if ("candidate".equals(historicIdentityLink.getType())) {
                        if (historicIdentityLink.getGroupId() != null) {
                            candidateGroup.add(historicIdentityLink.getGroupId());
                        }
                        if (historicIdentityLink.getUserId() != null) {
                            candidateUser.add(historicIdentityLink.getUserId());
                        }
                    }
                });
                if (candidateGroup.size() > 0) {
                    info.put("candidateGroup", candidateGroup);
                }
                if (candidateUser.size() > 0) {
                    info.put("candidateUser", candidateUser);
                }
            }

            // 3. 放入 审批表单数据
            Map<String, Object> taskLocalVariables = historicTaskInstance.getTaskLocalVariables();
            taskLocalVariables.remove("lastAssignee");
            if (taskLocalVariables.keySet().size() > 0) {
                info.put("formParams", historicTaskInstance.getTaskLocalVariables());
            }

            // 4. 放入评论
            List<Comment> comments = ActivitiUtils.getTaskService().getTaskComments(historicTaskInstance.getId());
            List<String> commentList = new ArrayList<>();
            comments.forEach(comment -> {
                commentList.add(comment.getFullMessage());
            });
            if (commentList.size() > 0) {
                info.put("comments", commentList);
            }

            //5.放入附件
            List<Attachment> attachments = ActivitiUtils.getTaskService().getTaskAttachments(historicTaskInstance.getId());
            if (attachments.size() > 0) {
                info.put("attachments", attachments);
            }

            historicTaskInfos.add(info);
        });
        // task 节点表格数据
        set("historicTaskInfos", historicTaskInfos);
        render("process/historicTaskInstances.ftl");
    }

    // 流程图查看器使用， 原 activiti 官方 "/process-definition/{processDefinitionId}/diagram-layout"
    public void definitionDiagramLayout() throws JsonProcessingException {
        String processDefinitionId = get("processDefinitionId");
        JSONObject jsonObject = DiagramUtils.getDiagramNode(processDefinitionId);
        String callback = getPara("callback", "");
        String json = callback + "(" + jsonObject.toJSONString() + ")";
        renderJson(json);
    }

    // 流程实例高亮线 原 activiti 官方"/process-instance/{processInstanceId}/highlights"
    // 业务中无用
    public void instanceHighlights() {
        String processInstanceId = get("processInstanceId");

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("processInstanceId", processInstanceId);
        ProcessInstance processInstance = (ProcessInstance) ActivitiUtils.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ActivitiUtils.getRepositoryService()
                .getProcessDefinition(processInstance.getProcessDefinitionId());
        responseJSON.fluentPut("processDefinitionId", processInstance.getProcessDefinitionId())
                .fluentPut("activities", ActivitiUtils.getRuntimeService().getActiveActivityIds(processInstanceId))
                .fluentPut("flows", DiagramUtils.getHighLightedFlows(processDefinition, processInstanceId));

        String callback = getPara("callback", "");
        String json = callback + "(" + responseJSON.toJSONString() + ")";
        renderJson(json);
    }
}

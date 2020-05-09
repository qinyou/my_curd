package com.github.qinyou.oa.activiti;

import com.google.common.base.Joiner;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.image.ProcessDiagramGenerator;

import java.io.InputStream;
import java.util.*;

/**
 * activiti 工具
 *
 * @author chuang
 */
@Slf4j
public class ActivitiKit {

    // activiti 引擎 以及 各个 service
    public static ProcessEngine getProcessEngine() {
        return ActivitiPlugin.processEngine;
    }
    public static RepositoryService getRepositoryService() {
        return ActivitiPlugin.processEngine.getRepositoryService();
    }
    public static FormService getFormService() {
        return ActivitiPlugin.processEngine.getFormService();
    }
    public static IdentityService getIdentityService() {
        return ActivitiPlugin.processEngine.getIdentityService();
    }
    public static RuntimeService getRuntimeService() {
        return ActivitiPlugin.processEngine.getRuntimeService();
    }
    public static TaskService getTaskService() {
        return ActivitiPlugin.processEngine.getTaskService();
    }
    public static HistoryService getHistoryService() {
        return ActivitiPlugin.processEngine.getHistoryService();
    }


    /**
     * 接收流程审批表单参数
     * 表单的 name 字段必须 是 "fp_" 开头
     * 例如 "fp_auditResult-审批结果"
     *
     * @param controller
     * @return
     */
    public static Map<String, Object> getFormParams(Controller controller) {
        Map<String, Object> ret = new LinkedHashMap<>();
        Map<String, String[]> properties = controller.getParaMap();
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            // 表单字段必须是 fp_ 开头
            if (!name.startsWith("fp_")) {
                continue;
            }
            if (value.length == 1) {
                ret.put(name, value[0]);
            } else {
                ret.put(name, Joiner.on(",").join(value));
            }
        }
        log.debug("form properties: {}", ret.toString());
        return ret;
    }


    /**
     * 查询 运行时 流程任务节点名
     *
     * @param businessKey
     * @return
     */
    public static String getActivityName(String businessKey) {
        String activityName = "未知";
        ProcessInstance instance = ActivitiKit.getRuntimeService().createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (instance != null) {
            ActivityImpl activity = ActivitiKit.getActivity(instance.getProcessDefinitionId(), instance.getActivityId());
            if (activity != null) {
                activityName = (String) activity.getProperty("name");
            }
        }
        return activityName;
    }


    /**
     * 根据 业务表单记录id 查询流程信息
     *
     * @param businessKey 业务表主键
     * @return
     */
    public static Map<String, Object> getInstanceInfoByBusinessKey(String businessKey) {
        Map<String, Object> ret = new HashMap<>();

        // 查询正在运行流程
        ProcessInstance instance = ActivitiKit.getRuntimeService().createProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        if (instance != null) {
            // 正运行流程 存在
            ret.put("processInstanceId", instance.getId()); // 流程实例id
            String definitionId = instance.getProcessDefinitionId();
            String activityId = instance.getActivityId();
            ret.put("processDefinitionId", definitionId); // 流程定义id
            ret.put("currentActivityId", activityId); // 当前任务节点 id
            ActivityImpl activity = ActivitiKit.getActivity(definitionId, activityId);
            if (activity != null) {
                ret.put("currentActivityName", activity.getProperty("name")); //当前任务节点名
            }
        } else {
            // 正运行流程 不存在  查询历史流程
            HistoricProcessInstance historicProcessInstance = ActivitiKit.getHistoryService().createHistoricProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();
            if (historicProcessInstance != null) {
                ret.put("hisProcessInstanceId", historicProcessInstance.getId());// 流程实例id
                ret.put("processDefinitionId", historicProcessInstance.getProcessDefinitionId());// 流程定义id
                ret.put("startTime", historicProcessInstance.getStartTime()); // 开始时间
                ret.put("endTime", historicProcessInstance.getEndTime()); // 结束时间
                ret.put("durationInMillis", historicProcessInstance.getDurationInMillis());// 持续时间
            }
        }
        // hisProcessInstanceId  和 processInstanceId 是 一致的
        return ret;
    }

    /**
     * 获得流程定义某节点
     *
     * @param processDefinitionId 流程定义id
     * @param activityId          任务节点id
     * @return
     */
    public static ActivityImpl getActivity(String processDefinitionId, String activityId) {
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ActivitiKit.getRepositoryService().getProcessDefinition(processDefinitionId);
        return pde.findActivity(activityId);
    }


    /**
     * 流程高亮跟踪图
     *
     * @param processInstanceId 流程实例Id
     * @return 图片输入流
     */
    public static InputStream getInstanceDiagram(String processInstanceId) {
        HistoricProcessInstance processInstance = ActivitiKit.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BpmnModel bpmnModel = ActivitiKit.getRepositoryService().getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessEngineConfiguration processEngineConfiguration = ActivitiKit.getProcessEngine().getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ActivitiKit.getRepositoryService().getProcessDefinition(processInstance.getProcessDefinitionId());
        List<HistoricActivityInstance> highLightedActivitList = ActivitiKit.getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        List<String> highLightedActivitis = new ArrayList<>();
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        // 中文乱码 解决
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "黑体", "黑体", "黑体", null, 1.0);
    }


    private static List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();
            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }


    /**
     * 查询运行时 流程实例
     *
     * @param processInstanceId       流程实例id
     * @param includeProcessVariables 是否包含流程变量
     * @return 流程实例
     */
    public static ProcessInstance getRuntimeProcessInstance(String processInstanceId, boolean includeProcessVariables) {
        ProcessInstanceQuery query = getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId);
        if (includeProcessVariables) {
            query.includeProcessVariables();
        }
        return query.singleResult();
    }
}

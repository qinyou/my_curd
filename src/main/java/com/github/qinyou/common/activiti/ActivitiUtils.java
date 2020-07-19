package com.github.qinyou.common.activiti;

import com.github.qinyou.common.activiti.image.ImageService;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.system.service.SysNoticeService;
import com.google.common.base.Joiner;
import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;

import java.util.*;

/**
 * activiti 工具
 *
 * @author chuang
 */
@Slf4j
public class ActivitiUtils {
    public static ProcessEngine getProcessEngine() {
        return ActivitiPlugin.processEngine;
    }
    public static RepositoryService getRepositoryService() {
        return ActivitiPlugin.processEngine.getRepositoryService();
    }

    private static ImageService imageService = new ImageService();
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


    // 发送消息通知工具
    private static SysNoticeService msgService = Duang.duang(SysNoticeService.class);
    public static void sendNotice(String username, String noticeTypeCode, Map<String,Object>params){
       msgService.sendNotice(username,noticeTypeCode,params);
    }
    public static void sendNotice(Set<String> usernameList, String noticeTypeCode, Map<String,Object>params){
        usernameList.forEach(username -> {
            msgService.sendNotice(username,noticeTypeCode,params);
        });
    }


    // 根据任务表单获取参数
    public static Map<String,String> getFormParams(Controller controller, String taskId){
        Map<String, String> ret = new LinkedHashMap<>();
        // 特殊的任务表单，定义在formKey 中
        String approve = controller.get("PROCESS_approve_审批结果");
        if(StringUtils.notEmpty(approve)){
            ret.put("PROCESS_approve_审批结果",approve);
        }
        List<FormProperty> formProperties = getFormService().getTaskFormData(taskId).getFormProperties();
        Set<String> taskFormIds = new HashSet<>();
        for (FormProperty formProperty : formProperties) {
            taskFormIds.add(formProperty.getId());
        }
        Map<String, String[]> properties = controller.getParaMap();
        for (Map.Entry<String, String[]> entry : properties.entrySet()) {
            String name = entry.getKey();
            if (!taskFormIds.contains(name)) {
                continue;
            }
            String[] value = entry.getValue();
            if (value.length == 1) {
                ret.put(name, value[0]);
            } else {
                ret.put(name, Joiner.on(",").join(value));
            }
        }
        log.debug("审批表单参数: {}", ret);
        return ret;
    }

    // 获得业务表单 当前节点信息
    public static Map<String, Object> getCurrent(String instanceId) {
        Map<String, Object> ret = new HashMap<>();
        List<Task> tasks = ActivitiUtils.getTaskService()
                .createTaskQuery()
                .processInstanceId(instanceId)
                .active()
                .orderByTaskCreateTime().desc().list();
        if(tasks.size()!=0){
            ret.put("key",tasks.get(0).getTaskDefinitionKey());
            ret.put("name",tasks.get(0).getName());
        }
        return ret;
    }

    /**
     * 获得流程定义某节点
     * @param processDefinitionId  流程定义id
     * @param activityId          节点id
     * @return
     */
    public static ActivityImpl getActivity(String processDefinitionId, String activityId) {
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ActivitiUtils.getRepositoryService().getProcessDefinition(processDefinitionId);
        return pde.findActivity(activityId);
    }


    /**
     * 流程高亮跟踪图
     *
     * @param processInstanceId 流程实例Id
     * @return 图片输入流
     */
    public static byte[] getInstanceDiagram(String processInstanceId) throws Exception {
        return imageService.getFlowImgByProcInstId(processInstanceId);
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

    /**
     * 查询历史 流程实例
     *
     * @param processInstanceId       流程实例id
     * @param includeProcessVariables 是否包含流程变量
     * @return 流程实例
     */
    public static HistoricProcessInstance getHistoricProcessInstance(String processInstanceId, boolean includeProcessVariables) {
        HistoricProcessInstanceQuery query = getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId);
        if (includeProcessVariables) {
            query.includeProcessVariables();
        }
        return query.singleResult();
    }

    /**
     * 获得任务办理表单
     *
     * @param task
     * @return
     */
    public static String getRenderedTaskFrom(Task task) {
        StringBuilder renderedForm = new StringBuilder(""); // 简单拼字符逻辑
        String formKey = task.getFormKey();
        if (StringUtils.notEmpty(formKey)) {
            // 常用表单简化
            renderedForm.append("<tr> <td>审批结果：</td><td> ");
            for (String key : formKey.split(",")) {
                String temp = null;
                switch (key) {
                    case "YES":
                        temp = "<input class='easyui-radiobutton'  checked='true' name='PROCESS_approve_审批结果' value='同意' label='同意' labelPosition='after' >";
                        break;
                    case "NO":
                        temp = "<input style='margin-right:50px' class='easyui-radiobutton' name='PROCESS_approve_审批结果' value='拒绝' label='拒绝' labelPosition='after' >";
                        break;
                }
                if (temp != null) {
                    log.info("temp: {}", temp);
                    renderedForm.append(temp);
                }
            }
            renderedForm.append("</td></tr>");
        } else {
            // 动态表单处理
            List<FormProperty> formProperties = getFormService().getTaskFormData(task.getId()).getFormProperties();
            for (FormProperty formProperty : formProperties) {
                // 日期类型渲染
                if (formProperty.getType() instanceof DateFormType) {
                    renderedForm.append("<tr><td>").append(formProperty.getName()).append("</td><td>");
                    String datePattern = (String) formProperty.getType().getInformation("datePattern");
                    String domClass;
                    // 仅支持 date 或 dateTime 常用格式
                    switch (datePattern) {
                        case "yyyy-MM-dd":
                            domClass = "easyui-datebox";
                            break;
                        case "yyyy-MM-dd HH:mm:ss":
                            domClass = "easyui-datetimebox";
                            break;
                        default:
                            domClass = "easyui-datetimebox";
                    }
                    renderedForm.append("<input class='")
                            .append(domClass).append("' name='")
                            .append(formProperty.getId()).append("' required='")
                            .append(formProperty.isRequired()).append("' >");
                    renderedForm.append("</td></tr>");
                }

                // 字符串类型渲染
                if (formProperty.getType() instanceof StringFormType) {

                    renderedForm.append("<tr><td>").append(formProperty.getName()).append("</td><td>");
                    renderedForm.append("<input style='width:90%; height:50px' class='easyui-textbox' name='")
                            .append(formProperty.getId()).append("' required='")
                            .append(formProperty.isRequired()).append("' >");
                    renderedForm.append("</td></tr>");
                }

                // 枚举渲染为下拉框
                if (formProperty.getType() instanceof EnumFormType) {

                    renderedForm.append("<tr><td>").append(formProperty.getName()).append("</td><td>");
                    renderedForm.append("<select panelHeight='auto' style='width:200px'  class='easyui-combobox' name='")
                            .append(formProperty.getId()).append("' required='")
                            .append(formProperty.isRequired()).append("' >");
                    ((Map<String, String>) formProperty.getType().getInformation("values")).forEach((key, value) -> {
                        renderedForm.append("<option value='").append(key).append("'>").append(value).append("</option>");
                    });
                    renderedForm.append("</select>").append("</td></tr>");
                }
                // 更多类型 更复杂业务 自行扩展
            }
        }
        return renderedForm.toString();
    }
}

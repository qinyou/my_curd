package com.github.qinyou.common.activiti.listener;


import com.github.qinyou.common.activiti.ActivitiUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 *  全局事件监听器
 *  监听任务创建，执行具体业务逻辑。
 * @author chuang
 */
@Slf4j
public class TaskAssignedEventListener implements ActivitiEventListener {
    private static final String TASK_ASSIGNED = "TASK_ASSIGNED";

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        if(ActivitiEventType.TASK_ASSIGNED.equals(eventType)){
            // 查询任务接收人
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
            TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();
            String assignee = taskEntity.getAssignee();

            //TODO 更复杂的业务逻辑，如自认领任务 不通知

            // 查询流程实例
            ProcessInstance instance = ActivitiUtils.getRuntimeProcessInstance(event.getProcessInstanceId(),false);
            Map<String,Object> params = new HashMap<>();
            params.put("instanceId",instance.getId());
            params.put("instanceName",instance.getName());
            params.put("taskName",taskEntity.getName());
            params.put("date", new DateTime(taskEntity.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));

            // 发送消息
            ActivitiUtils.sendNotice(assignee, TASK_ASSIGNED,params);
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}


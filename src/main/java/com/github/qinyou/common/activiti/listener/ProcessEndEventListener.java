package com.github.qinyou.common.activiti.listener;

import com.github.qinyou.common.activiti.ActivitiUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局事件监听器
 * 流程正常运行结束，给发起人发送消息通知
 * @author chuang
 */
@Slf4j
public class ProcessEndEventListener implements ActivitiEventListener {
    private static final String MSG_CODE = "PROCESS_COMPLETED";

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        if(ActivitiEventType.PROCESS_COMPLETED.equals(eventType)){
            HistoricProcessInstance instance = ActivitiUtils.getHistoricProcessInstance(event.getProcessInstanceId(),false);
            String username = instance.getStartUserId();
            Map<String,Object> params = new HashMap<>();
            params.put("instanceName",instance.getName());
            params.put("instanceId",instance.getId());
            ActivitiUtils.sendNotice(username, MSG_CODE,params);
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}

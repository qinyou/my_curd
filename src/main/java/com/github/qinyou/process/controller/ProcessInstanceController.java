package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.web.BaseController;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例 controller
 *
 * @author chuang
 */

@SuppressWarnings("Duplicates")
@RequirePermission("processInstance")
public class ProcessInstanceController extends BaseController {

    public void index() {
        render("process/processInstance.ftl");
    }

    /**
     * 正运行流程数据
     */
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        HistoricProcessInstanceQuery query = ActivitiUtils.getHistoryService().createHistoricProcessInstanceQuery();

        // 申请人
        String applyUser = getPara("extra_applyUser");
        if (StringUtils.notEmpty(applyUser)) {
            query.startedBy(applyUser);
        }

        // 流程id
        String instanceId = get("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }

        // 流程名
        String instanceName = get("extra_instanceName");
        if (StringUtils.notEmpty(instanceName)) {
            query.processInstanceNameLike("%" + instanceName + "%");
        }

        // 是否结束
        Boolean finished = getBoolean("extra_finished");
        if(finished!=null){
            if(finished){
                query.finished();
            }else{
                query.unfinished();
            }
        }

        // 流程定义id
        String definitionId = get("extra_definitionId");
        if (StringUtils.notEmpty(definitionId)) {
            query.processDefinitionId(definitionId);
        }

        // 流程定义key
        String definitionKey = get("extra_definitionKey");
        if (StringUtils.notEmpty(definitionKey)) {
            query.processDefinitionKey(definitionKey);
        }

        Long total = query.count();
        List<HistoricProcessInstance> list = query.orderByProcessInstanceStartTime().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize);
        List<Map<String, Object>> ret = new ArrayList<>();
        for (HistoricProcessInstance instance : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("definitionId", instance.getProcessDefinitionId());
            item.put("definitionKey", instance.getProcessDefinitionKey());

            item.put("instanceId", instance.getId());
            item.put("instanceName", instance.getName());
            item.put("startUser", instance.getStartUserId());
            item.put("startTime", instance.getStartTime());

            if(instance.getEndTime()!=null){
                item.put("endTime", instance.getEndTime());
                if(instance.getDeleteReason()!=null){
                    item.put("delReason", instance.getDeleteReason());
                }
            }
            ret.add(item);
        }

        renderDatagrid(ret, total.intValue());
    }
}

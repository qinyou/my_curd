package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiKit;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

import java.util.List;

/**
 * 流程实例 controller
 *
 * @author chuang
 */
@RequirePermission("processInstance")
public class ProcessInstanceController extends BaseController {

    public void index() {
        render("oa/processInstance.ftl");
    }

    /**
     * 正运行流程数据
     */
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);
        boolean finished = getParaToBoolean("finished", false);  // 是否结束

        HistoricProcessInstanceQuery query = ActivitiKit.getHistoryService().createHistoricProcessInstanceQuery();

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

        // 流程定义id
        String definitionId = getPara("extra_definitionId");
        if (StringUtils.notEmpty(definitionId)) {
            query.processDefinitionId(definitionId);
        }

        // 流程定义key
        String definitionKey = getPara("extra_definitionKey");
        if (StringUtils.notEmpty(definitionKey)) {
            query.processDefinitionKey(definitionKey);
        }

        // 业务表主键
        String businessKey = getPara("extra_businessKey");
        if (StringUtils.notEmpty(businessKey)) {
            query.processInstanceBusinessKey(businessKey);
        }

        if (finished) {
            query.finished();
        } else {
            query.unfinished();
        }

        Long total = query.count();
        List<HistoricProcessInstance> list = query.orderByProcessInstanceStartTime().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize);

        if (!finished) {
            for (HistoricProcessInstance instance : list) {
                instance.setLocalizedDescription(ActivitiKit.getActivityName(instance.getBusinessKey()));
                //instance.getProcessVariables().put("activityName",ActivitiKit.getActivityName(instance.getBusinessKey()));
            }
        }
        renderDatagrid(list, total.intValue());
    }
}

package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已办任务
 */
@SuppressWarnings("Duplicates")
@RequirePermission("myCompleteTask")
public class MyCompleteTaskController extends BaseController {
    public void index() {
        render("process/myCompleteTask.ftl");
    }

    // 表格数据
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        HistoricTaskInstanceQuery query = ActivitiUtils.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskAssignee(WebUtils.getSessionUsername(this))
                .finished();

        String instanceId = getPara("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }
        String taskName = getPara("extra_taskName");
        if (StringUtils.notEmpty(taskName)) {
            query.taskNameLike("%" + taskName + "%");
        }
        Long total = query.count();
        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByProcessInstanceId().desc() // 流程实例id 和 发起时间正相关，所以此处排序无问题
                .orderByHistoricTaskInstanceEndTime().asc()
                .listPage((pageNumber - 1) * pageSize, pageSize).forEach(task -> {
            Map<String, Object> info = new HashMap<>();
            info.put("id", task.getId());             // 任务id
            info.put("startTime",task.getStartTime());// 任务开始时间
            info.put("endTime", task.getEndTime());   // 任务完成时间
            info.put("taskName", task.getName());     // 任务名称
            info.put("duration", task.getDurationInMillis()); // 任务持续时长（毫秒）
            info.put("instanceId", task.getProcessInstanceId());  // 流程实例id
            HistoricProcessInstance instance = ActivitiUtils.getHistoricProcessInstance(task.getProcessInstanceId(),false);
            info.put("instanceName",instance.getName());
            info.put("startUser",instance.getStartUserId());  // 流程发起人
            list.add(info);
        });
        renderDatagrid(list, total.intValue());
    }
}

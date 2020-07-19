package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的 代办候选任务
 *
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("myCandidateTask")
public class MyCandidateTaskController extends BaseController {

    public void index() {
        render("process/myCandidateTask.ftl");
    }

    // 表格数据
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        TaskQuery query = ActivitiUtils.getTaskService().createTaskQuery()
                .taskCandidateUser(WebUtils.getSessionUsername(this))//自动调用候选组 和 候选人查询
                .active();

        String instanceId = getPara("extra_instanceId");
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }
        String taskName = getPara("extra_taskName");
        if (StringUtils.notEmpty(taskName)) {
            query.taskNameLike("%" + taskName + "%");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByTaskCreateTime().desc().listPage((pageNumber - 1) * pageSize, pageSize).forEach(task -> {
            Map<String, Object> info = new HashMap<>();
            info.put("id", task.getId()); // 任务id
            info.put("taskCreateTime", task.getCreateTime());
            info.put("taskName", task.getName());
            info.put("taskKey", task.getTaskDefinitionKey());
            info.put("instanceId", task.getProcessInstanceId());

            ActivitiUtils.getRuntimeService();
            ProcessInstance instance = ActivitiUtils.getRuntimeProcessInstance(task.getProcessInstanceId(), false);
            info.put("instanceName", instance.getName());
            list.add(info);
        });

        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }


    // 任务认领
    public void claimAction() {
        String taskId = get("taskId");
        if (StringUtils.isEmpty(taskId)) {
            renderFail("参数缺失");
            return;
        }
        String username = WebUtils.getSessionUsername(this);
        long count = ActivitiUtils.getTaskService().createTaskQuery()
                .taskCandidateUser(username)
                .taskId(taskId)
                .count();
        if (count == 0L) {
            renderFail("无权认领该任务");
            return;
        }
        try {
            ActivitiUtils.getTaskService().claim(taskId, username);
        } catch (ActivitiTaskAlreadyClaimedException e) {
            renderFail("任务已被认领");
            return;
        } catch (ActivitiObjectNotFoundException e2) {
            renderFail("任务不存在");
            return;
        }
        renderSuccess("认领完成");
    }

}

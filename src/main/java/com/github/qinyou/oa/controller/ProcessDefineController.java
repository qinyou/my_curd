package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.ProcessDefinitionInfo;
import com.jfinal.aop.Before;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequirePermission("processDefine")
public class ProcessDefineController extends BaseController {

    public void index() {
        render("oa/processDefine.ftl");
    }

    // 流程定义数据
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);

        String processKey = getPara("extra_key");

        RepositoryService service = ActivitiKit.getRepositoryService();
        ProcessDefinitionQuery query = service.createProcessDefinitionQuery();
        if (StringUtils.notEmpty(processKey)) {
            query.processDefinitionKeyLike("%" + processKey + "%");
        }

        List<ProcessDefinitionInfo> list = new ArrayList<>();
        query.orderByProcessDefinitionKey().orderByProcessDefinitionVersion().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize).forEach(processDefinition -> {
            ProcessDefinitionInfo info = new ProcessDefinitionInfo()
                    .setId(processDefinition.getId())
                    .setKey(processDefinition.getKey())
                    .setVersion(processDefinition.getVersion())
                    .setName(processDefinition.getName())
                    .setCategory(processDefinition.getCategory())
                    .setDescription(processDefinition.getDescription())
                    .setDeploymentId(processDefinition.getDeploymentId());
            info.setStatue(service.isProcessDefinitionSuspended(processDefinition.getId()) ? "挂起" : "激活");
            list.add(info);
        });

        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }


    // 激活操作
    @Before(IdsRequired.class)
    public void activateProcessDefine() {
        boolean activateProcessInstances = getParaToBoolean("activateProcessInstances", true);
        RepositoryService service = ActivitiKit.getRepositoryService();
        for (String id : getPara("ids").split(",")) {
            if (service.isProcessDefinitionSuspended(id)) {
                service.activateProcessDefinitionById(id, activateProcessInstances, null);
            }
        }
        renderSuccess("激活操作成功");
    }

    // 挂起操作
    @Before(IdsRequired.class)
    public void suspendProcessDefine() {
        boolean activateProcessInstances = getParaToBoolean("activateProcessInstances", true);
        RepositoryService service = ActivitiKit.getRepositoryService();
        for (String id : getPara("ids").split(",")) {
            if (!service.isProcessDefinitionSuspended(id)) {
                service.suspendProcessDefinitionById(id);
            }
        }
        renderSuccess("挂起操作成功");
    }
}

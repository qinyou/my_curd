package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.ProcessInstanceInfo;
import com.jfinal.kit.StrKit;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的流程
 *
 * @author chuang
 */
@RequirePermission("myProcess")
public class MyProcessController extends BaseController {

    /**
     * 列表页面
     */
    public void index() {
        render("oa/myProcess.ftl");
    }

    /**
     * 列表页面数据
     */
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);
        String username = WebUtils.getSessionUsername(this);
        HistoricProcessInstanceQuery query = ActivitiKit.getHistoryService().createHistoricProcessInstanceQuery()
                .notDeleted();
        query.startedBy(username); // 流程发起人

        String processName = get("extra_processName"); // 流程实例名
        String instanceId = getPara("extra_instanceId"); // 流程实例id
        if (StringUtils.notEmpty(processName)) {
            query.processInstanceNameLike("%" + processName + "%");
        }
        if (StringUtils.notEmpty(instanceId)) {
            query.processInstanceId(instanceId);
        }

        Long total = query.count();
        List<HistoricProcessInstance> list = query.orderByProcessInstanceStartTime().desc()
                .includeProcessVariables().listPage((pageNumber - 1) * pageSize, pageSize);

        List<ProcessInstanceInfo> retList = new ArrayList<>();
        for (HistoricProcessInstance instance : list) {
            ProcessInstanceInfo info = new ProcessInstanceInfo();
            info.setProcessInstanceId(instance.getId());
            info.setStartTime(instance.getStartTime());
            info.setEndTime(instance.getEndTime());
            info.setName(instance.getName());
            if (info.getEndTime() == null) {
                info.setActivityName(ActivitiKit.getActivityName(instance.getBusinessKey()));
            }
            info.setBusinessForm((String) instance.getProcessVariables().get("businessForm"));
            info.setBusinessKey(instance.getBusinessKey());
            retList.add(info);
        }
        renderDatagrid(retList, total.intValue());
    }

    /**
     * 新建流程弹窗
     */
    public void newProcess() {
        render("oa/businessFormList.ftl");
    }

    /**
     * 新建流程弹窗 重定向 业务表 新增表单
     */
    public void newProcessStep2() {
        String businessFormInfoId = getPara("businessFormInfoId");
        String formKey = getPara("formKey");
        if (StringUtils.isEmpty(businessFormInfoId) || StringUtils.isEmpty(formKey)) {
            renderFail("businessFormInfoId 或 formKey 参数缺失");
            return;
        }
        formKey = StrKit.toCamelCase(formKey);
        redirect("/" + formKey + "/newModel?businessFormInfoId=" + businessFormInfoId);
    }

    /**
     * 重定向 到 业务表单数据删除
     */
    public void deleteProcess() {
        String formKey = getPara("formKey");        // 业务表名
        String businessKey = getPara("businessKey"); // 业务表主键
        if (StringUtils.isEmpty(formKey) || StringUtils.isEmpty(businessKey)) {
            renderFail("businessKey 或 businessForm 参数缺失");
            return;
        }
        formKey = StrKit.toCamelCase(formKey);
        redirect("/" + formKey + "/deleteAction?id=" + businessKey);
    }
}

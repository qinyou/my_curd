package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActConst;
import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.process.model.ActFormTpl;
import com.github.qinyou.process.model.ActReProcdefCate;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("myApply")
public class MyApplyController extends BaseController {

    // 列表页
    public void index() {
        render("process/myApply.ftl");
    }

    // 已发起流程分类数据
    public void listCate() {
        String sql = "select b.KEY_ as processKey, c.name, c.pid, d.name as pName, count(1) as c from act_hi_procinst a " +
                "left join act_re_procdef b on a.PROC_DEF_ID_ = b.ID_ " +
                "left join act_re_procdef_cate c on b.KEY_ = c.defKey " +
                "left join act_re_procdef_cate d on c.pid = d.id " +
                "where a.START_USER_ID_ = ? GROUP BY b.KEY_, c.name, c.remark, c.pid";
        List<Record> records= Db.find(sql,WebUtils.getSessionUsername(this));
        renderJson(records);
    }

    // 流程分类列表页
    public void goCategoryList(){
        String sql = "select id,name,remark from act_re_procdef_cate where pid = '0' and state = '1'";
        List<ActReProcdefCate> records = ActReProcdefCate.dao.find(sql);
        for(ActReProcdefCate record : records){
            sql = "select name,remark,defKey,icon from act_re_procdef_cate where pid = ? and state = '1' ";
            List<ActReProcdefCate> items = ActReProcdefCate.dao.find(sql,record.getStr("id"));
            record.put("processList", items);
        }
        set("categoryList",records);
        render("process/myApply_categoryList.ftl");
    }

    // 表格数据
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        HistoricProcessInstanceQuery query = ActivitiUtils.getHistoryService().createHistoricProcessInstanceQuery();
        // 是否结束任务
        Boolean finished = getBoolean("extra_finished");
        if(finished!=null){
            if(finished){
                query.finished();
            }else{
                query.unfinished();
            }
        }

        // 查询当前用户的流程实例
        String username = WebUtils.getSessionUsername(this);
        query.startedBy(username);

        // 流程定义key
        String definitionKey = get("extra_definitionKey");
        if (StringUtils.notEmpty(definitionKey)) {
            query.processDefinitionKey(definitionKey);
        }

        // 流程id
        String instanceId = get("extra_instanceId");
        if(StringUtils.notEmpty(instanceId)){
            query.processInstanceId(instanceId);
        }

        // 流程名称
        String instanceName = get("extra_instanceName");
        if(StringUtils.notEmpty(instanceName)){
            query.processInstanceNameLike("%"+instanceName+"%");
        }

        Long total = query.count();
        List<HistoricProcessInstance> list = query.orderByProcessInstanceStartTime().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize);
        List<Map<String, Object>> ret = new ArrayList<>();

        for (HistoricProcessInstance instance : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("instanceId", instance.getId());
            item.put("instanceName", instance.getName());
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


    // 新建流程 表单页面
    public void newApply() {
        String processKey = get("processKey"); // 流程定义key
        if (StringUtils.isEmpty(processKey)) {
            renderFail("参数缺失");
            return;
        }
        set("test",get("test"));
        ProcessDefinition processDefinition = ActivitiUtils.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        if (processDefinition == null) {
            log.info("processDefinitionKey:{} latestVersion 不存在", processKey);
            renderFail("参数错误");
            return;
        }
        String startFormKey = ActivitiUtils.getFormService().getStartFormKey(processDefinition.getId());
        if (StringUtils.notEmpty(startFormKey)) {
            ActFormTpl formTpl = ActFormTpl.dao.findLatestActiveByKey(startFormKey);
            if (formTpl == null) {
                log.info("act_form_tpl fKey:{} latestVersion 不存在", startFormKey);
                renderFail("参数错误");
                return;
            }
            // 申请表单模板id
            setAttr("applyFormTplId", formTpl.getId());
            // 申请表单模板
            setAttr("applyFormTpl", formTpl.getTemplate());
            // 申请表单所引用的插件
            setAttr("applyFormTplPlugins", formTpl.getPlugins());
        }

        // 流程定义key
        setAttr("processKey", processKey);
        // 预设的申请名称 （流程定义名+申请人+日期)
        setAttr("applyFormTitle", processDefinition.getName() + "-"
                + WebUtils.getSysUser(this).getRealName() + "-"
                + new DateTime().toString("yyyy/MM/dd")
        );
        render("process/myApply_form.ftl");
    }

    // 新建申请 action
    public void newApplyAction() {
        String processKey = get("processKey");
        String applyFormTplId = get("applyFormTplId");
        String applyFormData = get("applyFormData");
        String applyFormTitle = get("applyFormTitle");

        if (StringUtils.isEmpty(processKey)
                || StringUtils.isEmpty(applyFormTplId)
                || StringUtils.isEmpty(applyFormData)
                || StringUtils.isEmpty(applyFormTitle)) {
            renderFail("参数缺失");
            return;
        }

        // 校验表单有效性
        ActFormTpl actFormTpl = ActFormTpl.dao.findById(applyFormTplId);
        if(actFormTpl==null){
            renderFail("参数错误");
            return;
        }
        if("0".equals(actFormTpl.getState())){
            renderFail("发起失败，该申请单已下线，请关闭页面后重新重新申请！");
            return;
        }

        Authentication.setAuthenticatedUserId(WebUtils.getSessionUsername(this));
        ActivitiUtils.getRuntimeService()
                .createProcessInstanceBuilder()
                .processDefinitionKey(processKey)
                .processInstanceName(applyFormTitle)
                .addVariable(ActConst.APPLY_FORM_DATA, applyFormData)
                .addVariable(ActConst.APPLY_FORM_TPL_ID, applyFormTplId)
                .start();
        renderSuccess("发起成功");
    }


    // 流程实例删除（仅运行结束实例、物理删除）
    @Before(IdRequired.class)
    public void deleteApply() {
        HistoricProcessInstance instance = ActivitiUtils.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(get("id")) // 流程实例id
                .finished()
                .singleResult();
        if(instance==null){
           renderFail("已结束流程实例 不存在");
           return;
        }
        ActivitiUtils.getHistoryService().deleteHistoricProcessInstance(instance.getId());
        renderSuccess("删除流程 成功");
    }


    // 流程实例取消 (仅未结束的流程、填充的作废原因）
    @Before(IdRequired.class)
    public void cancelApply(){
        HistoricProcessInstance instance = ActivitiUtils.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(get("id"))// 流程实例id
                .unfinished()
                .singleResult();
        if(instance==null){
            renderFail("进行中流程实例 不存在");
            return;
        }
        ActivitiUtils.getRuntimeService().deleteProcessInstance(instance.getId(),ActConst.DEL_USER_CANCEL);
        renderSuccess("取消流程 成功");
    }
}

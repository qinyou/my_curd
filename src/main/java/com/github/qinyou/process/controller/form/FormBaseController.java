package com.github.qinyou.process.controller.form;

import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.web.BaseController;
import com.jfinal.aop.Before;
import com.jfinal.core.NotAction;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Optional;

/**
 * 业务表单 controller 基础类
 */
public abstract class FormBaseController extends BaseController {

    protected final static String CREATE_PROCESS_SUCCESS = "流程创建成功";

    protected final static String ADJUST_FORM_SUCCESS = "调整成功";
    protected final static String FORBIDDEN_ADJUST = "当前阶段禁止调整";

    protected final static String DELETE_FORM_SUCCESS = "删除作废成功";
    protected final static String FORBIDDEN_DELETE = "已结束流程禁止删除!";

    /**
     * 发起流程
     *
     * @param processKey 流程 key
     * @param formId     业务表数据主键
     * @param formName   业务表名
     */
    @NotAction
    @Before(Tx.class)
    protected void createProcess(String processKey, String formId, String formName, Date createTime) {
        ProcessDefinition definition = ActivitiUtils.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .latestVersion()
                .singleResult();
        Optional.of(definition);

        String processInstanceName =
                definition.getName() + "(" + WebUtils.getSysUser(this).getRealName()
                        + " - " + new DateTime(createTime).toString("yyyy/MM/dd HH:mm:ss") + ")";
        Authentication.setAuthenticatedUserId(WebUtils.getSessionUsername(this)); // 填充流程发起人参数
        ProcessInstanceBuilder builder = ActivitiUtils.getRuntimeService()
                .createProcessInstanceBuilder()
                .processDefinitionKey(processKey)
                .businessKey(formId)
                .processInstanceName(processInstanceName)
                .addVariable("businessForm", formName);

        // 此处如果抛出异常，代表 processKey 流程不存在
        builder.start();
    }
}

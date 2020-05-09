package com.github.qinyou.oa.controller.form;

import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.controller.OAFormBaseController;
import com.github.qinyou.oa.model.BusinessFormInfo;
import com.github.qinyou.oa.model.FormLeave;
import com.github.qinyou.system.model.SysUser;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.activerecord.tx.TxConfig;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * form_leave 业务表控制器
 *
 * @author zhangchuang
 */
public class FormLeaveController extends OAFormBaseController {

    /**
     * 新增 或者 修改 弹窗
     */
    public void newModel() {
        String id = getPara("id");  // 业务表主键
        if (StringUtils.notEmpty(id)) {
            // 编辑 （流程表单调整)
            FormLeave formLeave = FormLeave.dao.findById(id);
            setAttr("formLeave", formLeave);
        } else {
            // 新增 （发起流程)
            String businessFormInfoId = getPara("businessFormInfoId");
            if (StringUtils.isEmpty(businessFormInfoId)) {
                renderFail("businessFormInfoId 参数缺失");
                return;
            }
            setAttr("businessFormInfoId", businessFormInfoId);
        }
        render("oa/form/formLeave_form.ftl");
    }


    /**
     * 新增 action
     */
    @TxConfig(ActivitiConfig.DATASOURCE_NAME)
    @Before(Tx.class)
    public void addAction() {
        // 保存业务表
        FormLeave formLeave = getBean(FormLeave.class, "");
        formLeave.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        formLeave.save();

        //发起流程
        String businessFormInfoId = getPara("businessFormInfoId");
        if (StringUtils.isEmpty(businessFormInfoId)) {
            renderFail("businessFormInfoId 参数为空");
            return;
        }
        BusinessFormInfo info = BusinessFormInfo.dao.findById(businessFormInfoId);
        if (info == null) {
            renderFail("businessFormInfoId 参数错误");
            return;
        }
        SysUser sysUser = WebUtils.getSysUser(this);
        String processInstanceName = info.getName() + "-( " + sysUser.getRealName()
                + new DateTime(formLeave.getCreateTime()).toString(" yyyy/MM/dd HH:mm )");
        Authentication.setAuthenticatedUserId(WebUtils.getSessionUsername(this));
        ProcessInstanceBuilder builder = ActivitiKit.getRuntimeService().createProcessInstanceBuilder()
                .processDefinitionKey(info.getProcessKey())
                .businessKey(formLeave.getId())
                .processInstanceName(processInstanceName)
                .addVariable("businessForm", info.getFormName());
        builder.start();

        renderSuccess(NEW_PROCESS_SUCCESS);
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        FormLeave formLeave = getBean(FormLeave.class, "");
        formLeave.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (formLeave.update()) {
            renderSuccess(ADJUST_FORM_SUCCESS);
        } else {
            renderFail(ADJUST_FORM_FAIL);
        }
    }

    /**
     * 逻辑 删除 action
     */
    @TxConfig(ActivitiConfig.DATASOURCE_NAME)
    @Before({IdRequired.class, Tx.class})
    public void deleteAction() {
        String id = getPara("id");     // 业务表主键
        FormLeave formLeave = FormLeave.dao.findById(id);
        // 不可删除非自己创建的申请
        if (!WebUtils.getSessionUsername(this).equals(formLeave.getCreater())) {
            throw new RuntimeException("非法删除流程操作");
        }
        ProcessInstance instance = ActivitiKit.getRuntimeService().createProcessInstanceQuery()
                .processInstanceBusinessKey(id)
                .singleResult();
        if (instance != null) {
            // 未结束流程 添加删除标志字段
            ActivitiKit.getRuntimeService().deleteProcessInstance(instance.getId(), ActivitiConfig.DEL_INSTANCE_BY_USER);
            formLeave.setDelFlag("Y").update();
            renderSuccess(DELETE_FORM_SUCCESS);
        } else {
            renderFail(DELETE_FORM_FAIL);
        }
    }


    /**
     * 表单详情
     */
    @Before(IdRequired.class)
    public void detail() {
        String id = getPara("id");  // 业务表id
        FormLeave formLeave = FormLeave.dao.findById(id);
        setAttr("formLeave", formLeave);
        render("oa/form/formLeave_detail.ftl");
    }
}

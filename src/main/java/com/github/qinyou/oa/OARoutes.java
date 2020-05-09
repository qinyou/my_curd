package com.github.qinyou.oa;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.oa.controller.*;
import com.github.qinyou.oa.controller.form.FormLeaveController;
import com.jfinal.config.Routes;

/**
 * oa 模块路由
 *
 * @author zhangchuang
 */
public class OARoutes extends Routes {

    @Override
    public void config() {
        // 通用
        add("/oa", OAController.class, Constant.VIEW_PATH);

        // 我的流程
        add("/myProcess", MyProcessController.class, Constant.VIEW_PATH);
        // 代办任务
        add("/myTask", MyTaskController.class, Constant.VIEW_PATH);
        // 候选任务
        add("/myCandidateTask", MyCandidateTaskController.class, Constant.VIEW_PATH);
        // 历史任务（流程)
        add("/myCompleteTask", MyCompleteTaskController.class, Constant.VIEW_PATH);

        // 业务表
        add("/businessFormInfo", BusinessFormInfoController.class, Constant.VIEW_PATH);
        // 流程部署
        add("/processDeploy", ProcessDeployController.class, Constant.VIEW_PATH);
        // 流程定义
        add("/processDefine", ProcessDefineController.class, Constant.VIEW_PATH);
        // 流程实例
        add("/processInstance", ProcessInstanceController.class, Constant.VIEW_PATH);

        //----------------- 业务表单开发 --------------------------
        // 简单请假流程
        add("/formLeave", FormLeaveController.class, Constant.VIEW_PATH);
    }
}

package com.github.qinyou.process;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.process.controller.*;
import com.jfinal.config.Routes;

/**
 * process 模块路由
 *
 * @author zhangchuang
 */
public class ProcessRoutes extends Routes {

    @Override
    public void config() {
        // 通用
        add("/process", ProcessController.class, Constant.VIEW_PATH);
        // 代办任务
        add("/myTask", MyTaskController.class, Constant.VIEW_PATH);
        // 候选任务
        add("/myCandidateTask", MyCandidateTaskController.class, Constant.VIEW_PATH);
        // 历史任务（流程)
        add("/myCompleteTask", MyCompleteTaskController.class, Constant.VIEW_PATH);
        // 流程部署
        add("/processDeploy", ProcessDeployController.class, Constant.VIEW_PATH);
        // 流程定义
        add("/processDefine", ProcessDefineController.class, Constant.VIEW_PATH);
        // 流程实例
        add("/processInstance", ProcessInstanceController.class, Constant.VIEW_PATH);
        // 流程模型管理
        add("/processModel", ProcessModelController.class, Constant.VIEW_PATH);
        // 表单模板管理
        add("/actFormTpl", ActFormTplController.class, Constant.VIEW_PATH);
        // 流程定义分类
        add("/actReProcdefCate", ActReProcdefCateController.class, Constant.VIEW_PATH);

        //----------------- 业务表单开发 --------------------------
        add("/myApply", MyApplyController.class, Constant.VIEW_PATH);
    }
}

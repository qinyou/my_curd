package com.github.qinyou.oa.controller;

import com.github.qinyou.common.web.BaseController;

/**
 * OA 业务表 controller 基础类
 */
public abstract class OAFormBaseController extends BaseController {
    protected final static String NEW_PROCESS_SUCCESS = "申请创建成功";
    protected final static String ADJUST_FORM_SUCCESS = "申请调整成功";
    protected final static String ADJUST_FORM_FAIL = "申请调整失败";
    protected final static String DELETE_FORM_SUCCESS = "申请删除成功";
    protected final static String DELETE_FORM_FAIL = "申请删除失败，已运行结束流程不可删除!";
}

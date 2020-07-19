package com.github.qinyou.common.activiti;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * activiti 常量
 *
 * @author chuang
 */
public class ActConst {
    // 删除原因： 用户取消申请
    public final static String DEL_USER_CANCEL = "USER_CANCEL";     // 申请用户取消
    public final static String DEL_MANAGER_CANCEL="MANAGER_CANCEL"; // 超管取消

    // 默认编码
    public final static Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    public final static String DEFAULT_FONT = "黑体";

    // 任务表单提交忽略的表单参数
    public final static Set<String> TASK_IG_FORM_KEYS = new HashSet<>();
    static{
        TASK_IG_FORM_KEYS.add("comment");
        TASK_IG_FORM_KEYS.add("taskId");
        TASK_IG_FORM_KEYS.add("instanceId");
        TASK_IG_FORM_KEYS.add("xmlHttpRequest");
        TASK_IG_FORM_KEYS.add("attachments");
    }

    // 任务表单参数作用域范围 前缀
    public final static String PROCESS_VAR_PREFIX = "PROCESS"; // 流程变量参数前缀
    public final static String TASK_VAR_PREFIX = "TASK";       // 任务变量参数前缀
    public final static String APPLY_VAR_PREFIX = "APPLY";     // 申请表单参数前缀

    // 调整表单任务节点名称
    public final static String ADJUST_APPLY_TASK_KEY = "AdjustApply";

    // 流程变量关键字
    public final static String APPLY_FORM_DATA = "APPLY_FORM_DATA";    // 申请表单shuju
    public final static String APPLY_FORM_TPL_ID = "APPLY_FORM_TPL_ID";// 申请表单模板id
    public final static String APPLY_USER = "initiator";               // 申请人
}



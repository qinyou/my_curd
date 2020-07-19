package com.github.qinyou.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.system.model.SysVisitLog;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * 访问日志拦截器
 */
@Slf4j
public class VisitLogInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        SysVisitLog sysVisitLog = new SysVisitLog();
        sysVisitLog.setId(IdUtils.id());
        sysVisitLog.setSysUserIp(WebUtils.getRemoteAddress(controller.getRequest()));
        sysVisitLog.setSysUser(WebUtils.getSessionUsername(controller));
        sysVisitLog.setUrl(inv.getActionKey());
        sysVisitLog.setRequestType(controller.getRequest().getMethod());
        sysVisitLog.setCreateTime(new Date());

        Map<String, String[]> params = controller.getRequest().getParameterMap();
        if (params.keySet().size() > 0) {
            sysVisitLog.setParam(JSON.toJSONString(params));
            if (sysVisitLog.getParam().length() > 500) {
                sysVisitLog.setParam("超长文本参数");
            }
        }
        sysVisitLog.save();
        inv.invoke();
    }
}

package com.github.qinyou.common.interceptor;

import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.utils.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Ret;
import com.jfinal.render.JsonRender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 全局异常 json, 附带 记录访问日志
 *
 * @author zhangchuang
 */
@Slf4j
public class ComActionInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        inv.getController().setAttr("setting", Constant.SETTING);

        String errMsg = null;
        try {
            inv.invoke();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            errMsg = ExceptionUtils.getMessage(e);
        }

        // 返回异常信息
        if (StringUtils.notEmpty(errMsg)) {
            String requestType = inv.getController().getRequest().getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(requestType) || StringUtils.notEmpty(inv.getController().getPara("xmlHttpRequest"))) {
                Ret ret = Ret.create().set("state", "error").set("msg", errMsg);
                inv.getController().render(new JsonRender(ret).forIE());
            } else {
                inv.getController().setAttr("errorMsg", errMsg);
                inv.getController().render(Constant.VIEW_PATH + "/common/500.ftl");
            }
        }
    }

}

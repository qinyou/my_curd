package com.github.qinyou.common.interceptor;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.system.service.SysNoticeService;
import com.jfinal.aop.Duang;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台 权限菜单 拦截器
 *
 * @author zhangchuang
 */
@Slf4j
public class PermissionInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        boolean flag = true;

        Controller controller = inv.getController();

        // 验证菜单权限
        RequirePermission requirePermission = controller.getClass().getAnnotation(RequirePermission.class);
        List<String> codes;
        if (requirePermission != null ) {
            codes = requirePermission.isResource() ?
                    controller.getSessionAttr("menuCodes") : controller.getSessionAttr("roleCodes");
            flag = codes.contains(requirePermission.value());
        }

        if (flag) {
            // 菜单权限通后 再验证按钮权限
            requirePermission = inv.getMethod().getAnnotation(RequirePermission.class);
            if (requirePermission != null) {
                codes = requirePermission.isResource() ?
                        controller.getSessionAttr("buttonCodes") : controller.getSessionAttr("roleCodes");
                flag = codes.contains(requirePermission.value());
            }
        }

        if (flag) {
            // 菜单权限、按钮权限 都具备 放行
            inv.invoke();
            return;
        }

        // ------------无权限-------------------

        // 推送消息
        String noticeTypeCode = "noPermissionOps";
        Map<String, Object> params = new HashMap<>();
        params.put("username", WebUtils.getSessionUsername(controller));
        params.put("visitUrl", controller.getRequest().getRequestURI());
        SysNoticeService service = Duang.duang(SysNoticeService.class);
        service.sendNotice(noticeTypeCode, params);

        // 响应
        String requestType = inv.getController().getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType) || StringUtils.notEmpty(inv.getController().getPara("xmlHttpRequest"))) {
            Ret ret = Ret.create().setFail().set("msg", "无权限操作！您的行为已被记录到日志。"); // 其实并没有，可以自行扩展
            controller.renderJson(ret);
        } else {
            controller.render("/WEB-INF/views/common/no_permission.ftl");
        }
    }
}

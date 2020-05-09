package com.github.qinyou.common.interceptor;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.jwt.JwtUtils;
import com.github.qinyou.common.utils.jwt.UserClaim;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * api 登录、授权、结果处理 拦截器
 * @author chuang
 */
@Slf4j
public class ApiInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        Ret ret;
        Controller controller = invocation.getController();
        HttpServletRequest req =  controller.getRequest();
        String JWTToken = req.getHeader("Authentication");
        JWTToken = JWTToken==null ? req.getParameter("token"):JWTToken;

        // 验证 token 是否存在
        if(StringUtils.isEmpty(JWTToken)){
            ret = Ret.fail().set("code",Result.NO_TOKEN.code)
                    .set("message",Result.NO_TOKEN.message);
            controller.renderJson(ret);
            return;
        }

        // 验证token 是否合法
        UserClaim userClaim;
        try{
            userClaim = JwtUtils.parseToken(JWTToken);
        }catch(ExpiredJwtException e){
            log.error(e.getMessage(),e);
            ret = Ret.fail().set("code",Result.EXPIRED_TOKEN.code).set("message",Result.EXPIRED_TOKEN.message);
            controller.renderJson(ret);
            return;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            ret = Ret.fail().set("code",Result.INVALID_TOKEN.code).set("message",Result.INVALID_TOKEN.message);
            controller.renderJson(ret);
            return;
        }

        // 验证用户权限
        boolean flag = true;
        RequirePermission requirePermission = invocation.getClass().getAnnotation(RequirePermission.class);
        if (requirePermission != null ) {
            flag = requirePermission.isPermission() ?
                    userClaim.getPermissionList().contains(requirePermission.value())
                    : userClaim.getRoleList().contains(requirePermission.value()) ;
        }
        if (flag) {
            requirePermission = invocation.getMethod().getAnnotation(RequirePermission.class);
            if (requirePermission != null) {
                flag = requirePermission.isPermission() ?
                        userClaim.getPermissionList().contains(requirePermission.value())
                        : userClaim.getRoleList().contains(requirePermission.value()) ;
            }
        }


        if(!flag){
            ret = Ret.fail().set("code",Result.NO_PERMISSION.code).set("message",Result.NO_PERMISSION.message);
            controller.renderJson(ret);
            return;
        }

        // token 用户信息 注入 到 方法参数中
        Object[] args = invocation.getArgs();
        for (int i=0;i<args.length;i++) {
            if (args[i] instanceof UserClaim){
                invocation.setArg(i,userClaim);
            }
            //TODO request body 注入到 controller 方法参数中
        }


        // 调用方法、请求结果封装
        try{
            invocation.invoke();
            ret = Ret.ok().set("code",Result.SUCCESS.code)
                    .set("message",Result.SUCCESS.message)
                    .set("data",invocation.getReturnValue());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            ret = Ret.fail().set("code",Result.ERROR.code)
                    .set("message",Result.ERROR.message+":"+e.getMessage());
        }

        controller.renderJson(ret);
    }


    /**
     * 结果封装
     */
    enum Result{
        NO_TOKEN(1001,"TOKEN缺失"),
        EXPIRED_TOKEN(1002,"TOKEN过期"),
        INVALID_TOKEN(1003,"TOKEN无效"),
        NO_PERMISSION(1004,"无权限"),
        ERROR(500,"系统异常"),
        SUCCESS(0,"调用成功");

        private Integer code;
        private String message;

        Result(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

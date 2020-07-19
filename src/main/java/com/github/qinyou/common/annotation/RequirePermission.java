package com.github.qinyou.common.annotation;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String value() default "";                 // 编码: 菜单 或 按钮 或 角色
    boolean isResource() default true;       // true 权限编码  false 角色编码
}

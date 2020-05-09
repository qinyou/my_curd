package com.github.qinyou.api;


import com.github.qinyou.api.controller.DemoController;
import com.jfinal.config.Routes;

/**
 * Api 模块路由
 *
 * @author zhangchuang
 */
public class ApiRoutes extends Routes {

    @Override
    public void config() {
        add("/demo", DemoController.class);
    }
}

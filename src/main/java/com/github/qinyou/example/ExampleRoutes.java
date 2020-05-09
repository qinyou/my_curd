package com.github.qinyou.example;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.example.controller.ExSingleTableController;
import com.github.qinyou.example.controller.ExStaffController;
import com.jfinal.config.Routes;

/**
 * System 模块路由配置
 *
 * @author zhangchuang
 */
public class ExampleRoutes extends Routes {

    @Override
    public void config() {
        add("/exSingleTable", ExSingleTableController.class, Constant.VIEW_PATH);

        add("/exStaff", ExStaffController.class, Constant.VIEW_PATH);
    }
}

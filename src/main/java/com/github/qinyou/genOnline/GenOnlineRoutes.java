package com.github.qinyou.genOnline;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.genOnline.controller.GenOnlineController;
import com.jfinal.config.Routes;

/**
 * 在线代码生成器 模块路由配置
 *
 * @author zhangchuang
 */
public class GenOnlineRoutes extends Routes {

    @Override
    public void config() {
        add("/genOnline", GenOnlineController.class, Constant.VIEW_PATH);
    }
}

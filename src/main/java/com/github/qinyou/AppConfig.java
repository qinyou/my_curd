package com.github.qinyou;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.github.qinyou.api.ApiRoutes;
import com.github.qinyou.common.activiti.ActivitiPlugin;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.*;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.log.LogBackLogFactory;
import com.github.qinyou.common.web.FileController;
import com.github.qinyou.common.web.LoginController;
import com.github.qinyou.common.web.MainController;
import com.github.qinyou.common.web.UtilsController;
import com.github.qinyou.example.ExampleModelMapping;
import com.github.qinyou.example.ExampleRoutes;
import com.github.qinyou.genOnline.GenOnlineRoutes;
import com.github.qinyou.process.ProcessModelMapping;
import com.github.qinyou.process.ProcessRoutes;
import com.github.qinyou.system.SystemModelMapping;
import com.github.qinyou.system.SystemRoutes;
import com.github.qinyou.system.model.SysUser;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.render.ViewType;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目启动类
 *
 * @author zhangcuang
 */
@Slf4j
public class AppConfig extends JFinalConfig {
    private static String configFile;
    static {
        String profile= PropKit.use("config.txt").get("profile");
        configFile = "config-"+profile+".txt";
        try{
            PropKit.append(PropKit.use("config-"+profile+".txt"));
            // 此处可载入更多配置文件
        }catch (IllegalArgumentException e){
            throw new RuntimeException(String.format("%s 配置文件不存在",configFile));
        }
        log.info("profile: {}",profile);
    }

    public static void main(String[] args) {
        UndertowServer.create(AppConfig.class,configFile)
                .configWeb(builder -> {
                    builder.addFilter("DruidWebStatFilter", "com.alibaba.druid.support.http.WebStatFilter");
                    builder.addFilterUrlMapping("DruidWebStatFilter", "/*");
                    builder.addFilterInitParam("DruidWebStatFilter", "exclusions", "*.js,*.gif,*.jpg,*.jpeg,*.png,*.css,*.ico,/druid/*,/static/*");
                    builder.addFilterInitParam("DruidWebStatFilter", "principalSessionName", "sysUserName");
                    builder.addFilterInitParam("DruidWebStatFilter", "profileEnable", "true");
                    builder.addWebSocketEndpoint("com.github.qinyou.common.ws.WebSocketServer");
                }).start();
    }


    @Override
    public void configConstant(Constants me) {
        me.setDevMode(PropKit.getBoolean("app.devMode"));
        me.setInjectDependency(true);               // 开启 jfinal inject 注解
        me.setLogFactory(new LogBackLogFactory());  // 使用 logback

        // 上传下载
        me.setBaseUploadPath(PropKit.get("file.upload"));
        me.setMaxPostSize(PropKit.getInt("file.maxPostSize"));
        me.setBaseDownloadPath(PropKit.get("file.download"));

        // 默认视图
        me.setViewType(ViewType.FREE_MARKER);
        me.setViewExtension(".ftl");
        me.setError403View(Constant.VIEW_PATH + "common/403.ftl");
        me.setError404View(Constant.VIEW_PATH + "common/404.ftl");
        me.setError500View(Constant.VIEW_PATH + "common/500.ftl");

        // json 格式化 和 解析
        me.setJsonFactory(MixedJsonFactory.me());
        me.setJsonDatePattern("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public void configRoute(Routes me) {
        me.add("/login", LoginController.class, Constant.VIEW_PATH);    // 登录、退出等
        me.add("/dashboard", MainController.class, Constant.VIEW_PATH); // 控制面板 首页
        me.add("/utils", UtilsController.class, Constant.VIEW_PATH);    // 页面工具, 如 选择角色、选择用户
        me.add("/file", FileController.class, Constant.VIEW_PATH);      //文件上传

        // 模块路由
        me.add(new SystemRoutes());    // 系统管理
        me.add(new ProcessRoutes());   // 工作流
        me.add(new GenOnlineRoutes()); // 代码生成器
        me.add(new ExampleRoutes());   // 开发功能测试
        me.add(new ApiRoutes());       // api 接口
    }


    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin dp = new DruidPlugin(PropKit.get("jdbc.url"), PropKit.get("jdbc.user"), PropKit.get("jdbc.password"), PropKit.get("jdbc.driver"));
        dp.setInitialSize(PropKit.getInt("jdbc.initialSize"));
        dp.setMaxActive(PropKit.getInt("jdbc.maxActive"));
        dp.setMinIdle(PropKit.getInt("jdbc.minIdle"));
        WallFilter wall = new WallFilter();
        wall.setDbType(PropKit.get("jdbc.dbType"));
        dp.addFilter(wall);
        dp.addFilter(new StatFilter());
        me.add(dp);
        log.info("Config Druid success");

        ActiveRecordPlugin ar = new ActiveRecordPlugin(dp);
        ar.setDialect(new MysqlDialect());
        ar.setShowSql(PropKit.getBoolean("app.showSql"));
        SystemModelMapping.mapping(ar);
        ProcessModelMapping.mapping(ar);
        ExampleModelMapping.mapping(ar);
        me.add(ar);
        log.info("Config ActiveRecord success");

        ActivitiPlugin ap = new ActivitiPlugin();
        me.add(ap);
        log.info("Config Activiti success");

        Cron4jPlugin cp = new Cron4jPlugin(PropKit.getProp(), "cron4j");
        me.add(cp);
        log.info("Config Cron4j success");

//        RedisPlugin userRedis = new RedisPlugin("user", configProp.get("redis.host"),configProp.getInt("redis.port")
//                ,configProp.getInt("redis.timeout"),configProp.get("redis.password"),configProp.getInt("redis.database"));
//        me.add(userRedis);
//        log.info("Config Redis success");
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new LoginInterceptor());       // 登录
        me.addGlobalActionInterceptor(new PermissionInterceptor());  // 权限

        // 访问日志拦截器
        Boolean visitLog = PropKit.getBoolean("setting.visitLog",true);
        if(PropKit.getBoolean("app.visitLog",false)){
            me.addGlobalActionInterceptor(new VisitLogInterceptor());
            log.info("Visit Log statue: OPEN");
        }

        // 部分 session 数据 放入 request, freemarker用于控制 dom渲染
        List<String> sessionFields = new ArrayList<>();
        sessionFields.add("sysUserName"); // 登录用户名
        sessionFields.add("buttonCodes"); // 按钮权限
        me.addGlobalActionInterceptor(new SessionInViewInterceptor(sessionFields));

        // 异常信息拦截器，可区分ajax 或 普通页面
        me.addGlobalActionInterceptor(new ComActionInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new UrlSkipHandler("^/ws-server", false)); // 放过 ws请求
        me.add(new ContextPathHandler("ctx"));                       // 视图中添加 应用上下文

        // druid 监控
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {
            public boolean isPermitted(HttpServletRequest request) {
                SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
                if (sysUser == null) {
                    return false;
                }
                return "admin".equals(sysUser.getUsername());
            }
        });
        me.add(dvh);
    }

    @Override
    public void configEngine(Engine me) {
    }

    @Override
    public void onStart() {
        super.onStart();
        StringUtils.printStartLog();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

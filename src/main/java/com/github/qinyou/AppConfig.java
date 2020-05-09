package com.github.qinyou;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.github.qinyou.api.ApiRoutes;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.web.LoginController;
import com.github.qinyou.common.web.MainController;
import com.github.qinyou.common.interceptor.*;
import com.github.qinyou.common.web.UtilsController;
import com.github.qinyou.common.utils.log.LogBackLogFactory;
import com.github.qinyou.common.web.FileController;
import com.github.qinyou.example.ExampleModelMapping;
import com.github.qinyou.example.ExampleRoutes;
import com.github.qinyou.genOnline.GenOnlineRoutes;
import com.github.qinyou.oa.OARoutes;
import com.github.qinyou.oa.OaModelMapping;
import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiPlugin;
import com.github.qinyou.system.SystemModelMapping;
import com.github.qinyou.system.SystemRoutes;
import com.github.qinyou.system.model.SysUser;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
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
 * Jfinal框架 配置
 *
 * @author zhangcuang
 */
@Slf4j
public class AppConfig extends JFinalConfig {
    public static final Prop configProp ;      // 配置文件
    private static final String activeProfile;  // 生效的配置环境

    static {
        Prop propTemp = PropKit.use("config.txt");
        activeProfile= propTemp.get("runtime.profile");
        if(!"dev".equalsIgnoreCase(activeProfile) && !"prod".equalsIgnoreCase(activeProfile)){
            log.info("配置文件错误，runtime.profile = {} , 仅可以配置为 dev 或 prod",activeProfile);
            throw new RuntimeException("config.txt 配置文件错误, runtime.profile  仅可以配置为 dev 或 prod");
        }
        configProp = PropKit.use("config-"+activeProfile+".txt");
        log.info("配置环境设置: {}",activeProfile);
    }

    public static void main(String[] args) {
        if(configProp==null){
            log.info("undertow server 启动失败, 配置文件加载异常。");
            return;
        }
        UndertowServer undertowServer = UndertowServer.create(AppConfig.class,"config-"+activeProfile+".txt")
                .configWeb(builder -> {
                    // druid web filter 配置
                    builder.addFilter("DruidWebStatFilter", "com.alibaba.druid.support.http.WebStatFilter");
                    builder.addFilterUrlMapping("DruidWebStatFilter", "/*");
                    builder.addFilterInitParam("DruidWebStatFilter", "exclusions", "*.js,*.gif,*.jpg,*.jpeg,*.png,*.css,*.ico,/druid/*,/static/*");
                    builder.addFilterInitParam("DruidWebStatFilter", "principalSessionName", "sysUserName");
                    builder.addFilterInitParam("DruidWebStatFilter", "profileEnable", "true");
                    // 配置 WebSocket，WebSocket 需使用 ServerEndpoint 注解
                    builder.addWebSocketEndpoint("com.github.qinyou.common.ws.WebSocketServer");
                });
        //log.info("in threads {}",undertowServer.getUndertowConfig().getIoThreads());
        //log.info("work threads {}",undertowServer.getUndertowConfig().getWorkerThreads());
        undertowServer.start();

        log.info("undertow server 启动成功");
    }


    @Override
    public void configConstant(Constants me) {
        if(activeProfile.equalsIgnoreCase("dev")){
            me.setDevMode(true);
        }
        // 使用自扩展的 logback
        me.setLogFactory(new LogBackLogFactory());
        // 开启 jfinal inject 注解
        me.setInjectDependency(true);

        // 上传下载
        me.setBaseUploadPath(configProp.get("file.upload"));
        me.setMaxPostSize(configProp.getInt("file.maxPostSize"));
        me.setBaseDownloadPath(configProp.get("file.download"));

        // 视图
        me.setViewType(ViewType.FREE_MARKER);
        me.setViewExtension(".ftl");
        me.setError403View(Constant.VIEW_PATH + "common/403.ftl");
        me.setError404View(Constant.VIEW_PATH + "common/404.ftl");
        me.setError500View(Constant.VIEW_PATH + "common/500.ftl");

        // jfinal json 和 fast json 混合
        me.setJsonFactory(MixedJsonFactory.me());
        me.setJsonDatePattern("yyyy-MM-dd HH:mm:ss");
    }


    @Override
    public void configRoute(Routes me) {
        // 公共路由
        me.add("/login", LoginController.class, Constant.VIEW_PATH);   // 登录
        me.add("/dashboard", MainController.class, Constant.VIEW_PATH); // 控制面板
        me.add("/utils", UtilsController.class, Constant.VIEW_PATH);  // 页面工具, 例如 选择角色、选择用户
        me.add("/file", FileController.class, Constant.VIEW_PATH);     // 公共文件上传

        // 模块路由
        me.add(new SystemRoutes());    // system 模块路由，菜单、角色、用户、组织机构 等等系统表信息管理
        me.add(new OARoutes());        // 整合 activiti 实现的流程审批功能
        me.add(new GenOnlineRoutes()); // 在线代码生成器
        me.add(new ExampleRoutes());   // 测试用例路由：代码生成的例子、实验性的东西
        me.add(new ApiRoutes()); // api 接口
    }


    @SuppressWarnings("Duplicates")
    @Override
    public void configPlugin(Plugins me) {
        // 数据源 1 (用户权限、组织机构、主数据 等) main
        DruidPlugin sysDruid = new DruidPlugin(configProp.get("jdbc.url"), configProp.get("jdbc.user"), configProp.get("jdbc.password"), configProp.get("jdbc.driver"));
        sysDruid.setInitialSize(configProp.getInt("jdbc.initialSize"));
        sysDruid.setMaxActive(configProp.getInt("jdbc.maxActive"));
        sysDruid.setMinIdle(configProp.getInt("jdbc.minIdle"));
        StatFilter statFilter = new StatFilter();
        WallFilter wall = new WallFilter();
        wall.setDbType(configProp.get("jdbc.dbType"));
        sysDruid.addFilter(statFilter);
        sysDruid.addFilter(wall);
        me.add(sysDruid);
        ActiveRecordPlugin sysActiveRecord = new ActiveRecordPlugin(sysDruid);
        sysActiveRecord.setDialect(new MysqlDialect());
        sysActiveRecord.setShowSql(activeProfile.equalsIgnoreCase("dev"));
        SystemModelMapping.mapping(sysActiveRecord);  // system 模块
        ExampleModelMapping.mapping(sysActiveRecord); // example 模块
        me.add(sysActiveRecord);
        log.info("设置 数据源 sysDruid sysActiveRecord 成功");


        // 数据源2 （activiti表、流程表单） my_curd_oa
        DruidPlugin oaDruid = new DruidPlugin(configProp.get("oa.jdbc.url"), configProp.get("oa.jdbc.user"), configProp.get("oa.jdbc.password"), configProp.get("oa.jdbc.driver"));
        oaDruid.setInitialSize(configProp.getInt("oa.jdbc.initialSize"));
        oaDruid.setMaxActive(configProp.getInt("oa.jdbc.maxActive"));
        oaDruid.setMinIdle(configProp.getInt("oa.jdbc.minIdle"));
        oaDruid.addFilter(statFilter);
        oaDruid.addFilter(wall);
        me.add(oaDruid);
        ActiveRecordPlugin oaActiveRecord = new ActiveRecordPlugin(ActivitiConfig.DATASOURCE_NAME,oaDruid);
        oaActiveRecord.setDialect(new MysqlDialect());
        oaActiveRecord.setShowSql(activeProfile.equalsIgnoreCase("dev"));
        OaModelMapping.mapping(oaActiveRecord);
        me.add(oaActiveRecord);
        log.info("设置 数据源 oaDruid oaActiveRecord 成功");

        // activiti 插件
        ActivitiPlugin ap = new ActivitiPlugin();
        me.add(ap);
        log.info("加载 Activiti 插件 成功");

        // 定时任务
        Cron4jPlugin cp = new Cron4jPlugin(configProp, "cron4j");
        me.add(cp);
        log.info("加载 Corn4j 插件 成功");

        //         redis 插件
//        RedisPlugin userRedis = new RedisPlugin("user", configProp.get("redis.host"),configProp.getInt("redis.port")
//                ,configProp.getInt("redis.timeout"),configProp.get("redis.password"),configProp.getInt("redis.database"));
//        me.add(userRedis);
//        log.info("加载redis 插件成功");

    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new LoginInterceptor());       // 登录 拦截器
        me.addGlobalActionInterceptor(new PermissionInterceptor());  // 权限 拦截器

        // 访问日志拦截器，访问url 写到数据表
        Boolean visitLog = configProp.getBoolean("sys.visitLog");
        if(visitLog){
            me.addGlobalActionInterceptor(new VisitLogInterceptor());
        }
        log.info("访问日志 拦截器状态: {}",visitLog);

        // session 数据 放入 request, 用于 控制 dom （例如控制 菜单按钮）
        List<String> sessionFields = new ArrayList<>();
        sessionFields.add("sysUserName");
        sessionFields.add("buttonCodes");
        me.addGlobalActionInterceptor(new SessionInViewInterceptor(sessionFields));

        // 统一异常信息拦截器
        me.addGlobalActionInterceptor(new ComActionInterceptor());
    }

    @SuppressWarnings("Convert2Lambda")  // 不用 lambda, 用 lambda 有 bug
    @Override
    public void configHandler(Handlers me) {
        // 跳过处理 websocket 请求
        me.add(new UrlSkipHandler("^/ws-server", false));
        // 视图中添加context路径 (应用上下文路径)
        me.add(new ContextPathHandler("ctx"));
        // druid 监控（只允许admin查看）
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

}

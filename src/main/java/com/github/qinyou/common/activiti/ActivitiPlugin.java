package com.github.qinyou.common.activiti;

import com.github.qinyou.common.activiti.listener.ProcessEndEventListener;
import com.github.qinyou.common.activiti.listener.TaskAssignedEventListener;
import com.github.qinyou.common.activiti.method.CommonMethod;
import com.github.qinyou.common.activiti.method.DirectorMethod;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *  Jfinal activiti 插件
 * @author chuang
 */
@Slf4j
public class ActivitiPlugin implements IPlugin {
    static ProcessEngine processEngine = null;

    @Override
    public boolean start() {
        try {
            createProcessEngine();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public boolean stop() {
        ProcessEngines.destroy();
        ActivitiPlugin.processEngine = null;
        return true;
    }

    /**
     * 创建工作流引擎
     *
     * @return
     */
    private void createProcessEngine() {
        if (processEngine != null) {
            return;
        }

        StandaloneProcessEngineConfiguration conf = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        conf.setDataSource(DbKit.getConfig().getDataSource())
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE) // 自动更新数据库表
                .setDbHistoryUsed(true);                                                  // 历史表生效功能
        conf.setTransactionsExternallyManaged(true); // 使用托管事务工厂(不配置，事务也可生效)
        conf.setTransactionFactory(new ActivitiTransactionFactory());

        // 流程图字体
        conf.setActivityFontName(ActConst.DEFAULT_FONT);
        conf.setLabelFontName(ActConst.DEFAULT_FONT);
        conf.setAnnotationFontName(ActConst.DEFAULT_FONT);

        // 注入流程中使用的 bean方法
        Map<Object, Object> beans = new HashMap<>();
        DirectorMethod directorMethod = new DirectorMethod();
        beans.put("directorMethod", directorMethod);
        CommonMethod commonMethod = new CommonMethod();
        beans.put("commonMethod", commonMethod);
        conf.setBeans(beans);

        // 整合 用户 用户组 到 系统用户 角色 （ 主要用于候选任务查询）
        List<SessionFactory> sessionFactories = new ArrayList<>();
        UserManagerFactory userManagerFactory = new UserManagerFactory();
        userManagerFactory.setUserManager(new UserManager());
        sessionFactories.add(userManagerFactory);
        GroupManagerFactory groupManagerFactory = new GroupManagerFactory();
        groupManagerFactory.setGroupManager(new GroupManager());
        sessionFactories.add(groupManagerFactory);
        conf.setCustomSessionFactories(sessionFactories);

        // 全局监听器
        Map<String, List<ActivitiEventListener>> typedListeners = new HashMap<>();
        // 1. 流程运行结束 通知申请人
        List<ActivitiEventListener> processEndListeners = new ArrayList<>();
        processEndListeners.add(new ProcessEndEventListener());
        typedListeners.put("PROCESS_COMPLETED",processEndListeners);
        // 2. 任务关联接收人后，通知处理人
        List<ActivitiEventListener> taskAssignedListeners = new ArrayList<>();
        taskAssignedListeners.add(new TaskAssignedEventListener());
        typedListeners.put("TASK_ASSIGNED",taskAssignedListeners);

        conf.setTypedEventListeners(typedListeners);

        ActivitiPlugin.processEngine = conf.buildProcessEngine();
    }
}

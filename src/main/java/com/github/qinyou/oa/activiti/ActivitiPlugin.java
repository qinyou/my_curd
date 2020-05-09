package com.github.qinyou.oa.activiti;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;

/***
 *  Jfinal activiti 插件
 * @author chuang
 */
@Slf4j
public class ActivitiPlugin implements IPlugin {
    public static ProcessEngine processEngine = null;

    @Override
    public boolean start() {
        try {
            createProcessEngine();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
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
     * @return
     */
    private Boolean createProcessEngine(){
        if (processEngine != null) {
            return true;
        }
        StandaloneProcessEngineConfiguration conf =
                (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        conf.setDataSource(DbKit.getConfig().getDataSource())
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE) // 自动更新数据库表
            .setDbHistoryUsed(true);                                                   // 历史表生效功能
        // conf.setTransactionsExternallyManaged(true); // 使用托管事务工厂(不配置，事务也可生效)


        conf.setTransactionFactory(new ActivitiTransactionFactory());

        ActivitiPlugin.processEngine = conf.buildProcessEngine();
        return true;
    }
}

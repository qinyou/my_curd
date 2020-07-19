package com.github.qinyou.process;

import com.github.qinyou.common.activiti.ActivitiPlugin;
import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.system.SystemModelMapping;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.TaskQuery;

/**
 * 非web 环境下 调试 activiti api
 */
@Slf4j
public class Test {

    static void init() {
        Prop jdbcProp = PropKit.use("config-dev.txt");
        DruidPlugin dp = new DruidPlugin(jdbcProp.get("jdbc.url"),
                jdbcProp.get("jdbc.user"), jdbcProp.get("jdbc.password"), jdbcProp.get("jdbc.driver"));
        dp.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(true);
        SystemModelMapping.mapping(arp);  // system 模块
        ProcessModelMapping.mapping(arp);
        arp.start();
        ActivitiPlugin ap = new ActivitiPlugin();
        ap.start();
    }

    static void unDeploy(String deployId){
        ActivitiUtils.getRepositoryService().deleteDeployment(deployId, true);
        log.info("unDeploy {} over", deployId);
    }

    static String startProcess() {
        String processKey = "test02";
        String instanceName = "测试并行网关";
        String applyUser = "admin";
        Authentication.setAuthenticatedUserId(applyUser); // 填充流程发起人参数
        RuntimeService runtimeService = ActivitiUtils.getRuntimeService();
        ProcessInstance instance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey(processKey)
                .processInstanceName(instanceName)
                //.addVariable("assigneeList", Arrays.asList(new String[]{"admin","zhangdali","zhaoli"}))
                .start();
        return instance.getId();
    }

    static void taskList(String username){
        TaskQuery query = ActivitiUtils.getTaskService().createTaskQuery();
        query.taskAssignee(username).active()
                .orderByTaskCreateTime().desc()
        .list().forEach(task -> {
            log.info("taskKey:{}",task.getTaskDefinitionKey());
            log.info("taskId:{}",task.getId());
            log.info("taskName:{}",task.getName());
            log.info("taskDate:{}",task.getCreateTime());
            log.info("instanceId:{}",task.getProcessInstanceId());
            log.info("executionId:{}",task.getExecutionId());

            Execution execution = ActivitiUtils.getRuntimeService().createExecutionQuery()
                    .executionId(task.getExecutionId()).singleResult();
            log.info("execution Name: {}",execution.getName());

            ProcessInstance instance = ActivitiUtils.getRuntimeService().createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId()).singleResult();
            log.info("instanceName: {}",instance.getName());
        });
    }

    static void completeTask(String taskId){
        ActivitiUtils.getTaskService().complete(taskId);
        log.info("complate task {} over",taskId);
    }

    public static void main(String[] args) throws Exception {
        init();
        //log.info("instanceId:{}", startProcess());
        //taskList("admin");
        //completeTask("577508");

        // 结论
        // 1. 会签任务查询 流程名称必须 用 task.getProcessInstanceId
        // 2. 并行网关测试 同  修改申请表参数 通过sql 修改更靠谱一点
        // 3. 子流程 TODO
    }
}

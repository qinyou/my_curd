package com.github.qinyou.oa;

import com.github.qinyou.oa.activiti.ActivitiConfig;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.activiti.ActivitiPlugin;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 非web 环境下 调试 activiti api
 */
@Slf4j
public class Test {

    static void init() {
        Prop jdbcProp = PropKit.use("config-dev.txt");
        DruidPlugin dp = new DruidPlugin(jdbcProp.get("oa.jdbc.url"),
                jdbcProp.get("oa.jdbc.user"), jdbcProp.get("oa.jdbc.password"), jdbcProp.get("oa.jdbc.driver"));
        dp.start();
        ActiveRecordPlugin arp = new ActiveRecordPlugin(ActivitiConfig.DATASOURCE_NAME, dp);
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(true);
        arp.start();
        ActivitiPlugin ap = new ActivitiPlugin();
        ap.start();
    }

    // 部署流程
    static void deployZip() throws FileNotFoundException {
        InputStream in = new FileInputStream("E:/leaveProcess.zip");
        String processName = "简单请假流程";
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = ActivitiKit.getProcessEngine().getRepositoryService()
                .createDeployment().addZipInputStream(zipInputStream)
                .name(processName)
                .deploy();
        log.info("id:{}", deployment.getId());
        log.info("name:{}", deployment.getName());
    }

    static void listProcessDefine() {
        List<ProcessDefinition> list = ActivitiKit.getRepositoryService().createProcessDefinitionQuery()
                .latestVersion().list();
        list.forEach(processDefinition -> {
            log.info(processDefinition.getKey());
            log.info(processDefinition.getId());
            log.info(processDefinition.getName());
            log.info(processDefinition.getCategory());
            log.info(processDefinition.getDescription());
            log.info(processDefinition.getVersion() + "");
            log.info(processDefinition.getTenantId());  // 租户
            log.info(processDefinition.getResourceName());
            log.info(processDefinition.getDiagramResourceName());
        });
    }

    public static void main(String[] args) throws Exception {
        //init();
        List<String> list = new ArrayList<>();
        list.add(null);
        System.out.println(list.size());
        System.out.println(list.get(0));
//        String processInstanceId = "7501";
//
//        System.out.println("--start------");
//        ActivitiKit.getHistoryService().createHistoricActivityInstanceQuery()
//                .processInstanceId(processInstanceId)
//                .list().forEach(historicActivityInstance -> {
//            System.out.println("开始时间:"+historicActivityInstance.getStartTime());
//            System.out.println("结束时间:"+historicActivityInstance.getEndTime());
//
//            System.out.println("getActivityName:"+historicActivityInstance.getActivityName());
//            System.out.println("getAssignee:"+historicActivityInstance.getAssignee());
//
//            System.out.println("getActivityType:"+historicActivityInstance.getActivityType());
//            System.out.println("getExecutionId:"+historicActivityInstance.getExecutionId());
//        });
//        System.out.println("--end------");
//         ActivitiKit.getHistoryService().createHistoricProcessInstanceQuery().startedBy("admin").list()
//                 .forEach(historicProcessInstance -> {
//             System.out.println("--------------------------------------------------");
//             System.out.println(historicProcessInstance.getName());
//             System.out.println(historicProcessInstance.getStartTime());
//             System.out.println(historicProcessInstance.getEndTime());
//             System.out.println();
//         });
    }
}

package com.github.qinyou.oa.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TaskInfo {
    // taskId
    private String id;
    // 任务名称
    private String taskName;
    // 任务名编码
    private String taskDefinitionKey;
    // 流程实例id
    private String processInstanceId;
    // 流程实例名
    private String processInstanceName;
    // 申请人
    private String initiator;
    // 任务创建时间
    private Date createTime;

}

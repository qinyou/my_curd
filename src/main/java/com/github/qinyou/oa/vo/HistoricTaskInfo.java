package com.github.qinyou.oa.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class HistoricTaskInfo {
    // 任务id
    private String id;
    // 任务名
    private String name;
    // 开始时间
    private Date startTime;
    // 结束时间
    private Date endTime;
    //创建时间
    private Date createTime;
    // 认领时间
    private Date claimTime;
    // 审批人
    private String assignee;


    // 原审批人（针对任务转办）
    private String lastAssignee;

    // 评论列表
    private List<String> comments;
    // 候选组
    private List<String> candidateGroup;
    // 候选用户
    private List<String> candidateUser;
}

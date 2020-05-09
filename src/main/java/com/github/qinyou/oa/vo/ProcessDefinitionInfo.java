package com.github.qinyou.oa.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessDefinitionInfo {
    private String id;
    private String key;
    private String name;
    private int version;
    private String description;
    private String deploymentId;
    private String category;
    private String statue; // 激活 或 挂起 两种状态
}

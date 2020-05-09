package com.github.qinyou.oa.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ProcessDeploymentInfo {
    String id;
    String name;
    String category;
    Date deploymentTime;
}

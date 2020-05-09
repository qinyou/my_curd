package com.github.qinyou.example;

import com.github.qinyou.example.model.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated MappingKit
 * example 模块数据表  MappingKit
 *
 * @author zhangchuang
 * @since 2019-02-22 21:37:49
 */
public class ExampleModelMapping {

    public static void mapping(ActiveRecordPlugin arp) {
        // 例子 单表结构
        arp.addMapping("ex_single_table", "id", ExSingleTable.class);

        // 员工教育经历
        arp.addMapping("ex_staff_education", "id", ExStaffEducation.class);
        // 员工工作经历
        arp.addMapping("ex_staff_experience", "id", ExStaffExperience.class);
        // 员工家人
        arp.addMapping("ex_staff_family", "id", ExStaffFamily.class);
        // 一线员工
        arp.addMapping("ex_staff", "id", ExStaff.class);
    }
}


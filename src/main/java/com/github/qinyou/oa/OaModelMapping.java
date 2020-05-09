package com.github.qinyou.oa;

import com.github.qinyou.oa.model.BusinessFormInfo;
import com.github.qinyou.oa.model.FormLeave;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated MappingKit
 * oa 模块数据表  MappingKit
 *
 * @author zhangchuang
 * @since 2019-07-23 15:50:30
 */
public class OaModelMapping {

    public static void mapping(ActiveRecordPlugin arp) {
        // 业务表定义信息
        arp.addMapping("business_form_info", "id", BusinessFormInfo.class);
        // 请假流程表单
        arp.addMapping("form_leave", "id", FormLeave.class);
    }
}


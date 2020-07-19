package com.github.qinyou.process;

import com.github.qinyou.process.model.ActFormTpl;
import com.github.qinyou.process.model.ActReProcdefCate;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated MappingKit
 * process 模块数据表  MappingKit
 *
 * @author zhangchuang
 * @since 2019-07-23 15:50:30
 */
public class ProcessModelMapping {

    public static void mapping(ActiveRecordPlugin arp) {
        // 审批流表单模板
        arp.addMapping("act_form_tpl", "id", ActFormTpl.class);
        // 流程定义分类
        arp.addMapping("act_re_procdef_cate", "id", ActReProcdefCate.class);
    }
}


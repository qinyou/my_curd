package com.github.qinyou.common.constant;

import com.github.qinyou.system.model.SysSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目公共常量
 * @author chuang
 */
public class Constant {
    // 项目默认编码
    public final static String DEFAULT_ENCODEING = "UTF-8";

    // 查询 过滤器 使用 SearchSql 使用 ( 查询条件放入 到 request attr 的 key名)
    public final static String SEARCH_SQL = "search_sql";
    // 查询表单字段前缀
    public final static String SEARCH_PREFIX = "search_";

    // views 视图路径
    public final static String VIEW_PATH = "/WEB-INF/views/";

    // 系统参数设置
    public final static Map<String, String> SETTING = new HashMap<>();
    static {
        List<SysSetting> sysSettings = SysSetting.dao.findAll();
        for (SysSetting sysSetting : sysSettings) {
            SETTING.put(sysSetting.getSettingCode(), sysSetting.getSettingValue());
        }
    }
}

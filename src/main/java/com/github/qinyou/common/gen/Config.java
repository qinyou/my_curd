package com.github.qinyou.common.gen;

import com.jfinal.plugin.activerecord.generator.TypeMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 特色配置项，如不清楚代码逻辑，不要轻易修改
 *
 * @author zhangchuang
 */
public class Config {

    public final static TypeMapping typeMapping = new TypeMapping(); // 数据表字段驱动内类型 和 Jfinal model 类型映射
    public static Map<String, String> getterTypeMap;
    public static Map<String, String> longShortMap;
    public static Set<String> excludeImportTypes;

    static {
        // java 类型 和  Jfinal Model get 方法映射
        getterTypeMap = new HashMap<>();
        getterTypeMap.put("String", "getStr");
        getterTypeMap.put("Integer", "getInt");
        getterTypeMap.put("Long", "getLong");
        getterTypeMap.put("Double", "getDouble");
        getterTypeMap.put("Float", "getFloat");
        getterTypeMap.put("Short", "getShort");
        getterTypeMap.put("Byte", "getByte");

        // 数据表字段 对应java类型长短名映射
        longShortMap = new HashMap<>();
        longShortMap.put("java.util.Date", "Date");
        longShortMap.put("java.lang.String", "String");
        longShortMap.put("java.math.BigDecimal", "BigDecimal");
        longShortMap.put("java.lang.Integer", "Integer");
        longShortMap.put("java.lang.Double", "Double");
        longShortMap.put("java.lang.Float", "Float");
        longShortMap.put("java.lang.Long", "Long");

        // 类中不需要引入的基础数据字段类型
        excludeImportTypes = new HashSet<>();
        excludeImportTypes.add("String");
        excludeImportTypes.add("Double");
        excludeImportTypes.add("Integer");
        excludeImportTypes.add("Float");
        excludeImportTypes.add("Long");
    }
}

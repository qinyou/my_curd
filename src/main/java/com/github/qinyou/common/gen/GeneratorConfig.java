package com.github.qinyou.common.gen;

import com.jfinal.kit.PathKit;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * 代码生成器配置
 *
 * @author zhangchuang
 */
public class GeneratorConfig {

    public static String moduleName = "system"; // 模块名
    public static String basePackageName = "com.github.qinyou"; // 基础包名
    public static String author = "chuang";  // 代码中 @author 注释

    public static String schemaPattern = "my_curd";   // 数据库 schema
    public static String ignoreTablePrefixes = "1act_"; // 忽略的表 前缀，多个之间 逗号分隔
    //数据表 除id外，列表页表单页不显示字段
    public static String[] excludeFields = new String[]{"createTime", "creater", "updateTime", "updater"};
    // 模板基础路径
    public static String tplBasePath = PathKit.getRootClassPath().replaceAll("\\\\", "/") + "/generator/";
    // 数据源名, 根据实际情况配置
    public static String dsName = "main"; // main 或 my_curd_oa


    //------------- web环境下不需要，通过main 方法调用----------------
    // 生成文件基础路径
    public static String outputBasePath = "E:/mycurdpro/gencode/" + GeneratorConfig.moduleName + "/";
    // 需生成代码的表
    public static Set<String> tableNames = new LinkedHashSet<>();
    static {
        // 此处添加需要生成的表名
        tableNames.add("sys_button");
        tableNames.add("sys_role_button");
    }
}

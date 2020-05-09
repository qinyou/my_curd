package com.github.qinyou.common.gen.client;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.gen.GeneratorConfig;
import com.github.qinyou.common.gen.tools.MysqlDataSourceUtils;
import com.github.qinyou.common.gen.tools.MysqlMetaUtils;
import com.github.qinyou.common.gen.tools.TableMeta;
import com.github.qinyou.common.utils.freemarker.FreemarkerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通单表操作代码生成器, Controller  Page
 * 如为 树表，自行修改生成后的代码
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class SingleTableClient {
    // 生成 controller
    private final static String controllerTplPath = GeneratorConfig.tplBasePath + "singletable/controller.ftl";// controller 模板文件路径
    // 生成 页面
    private final static String indexTplPath = GeneratorConfig.tplBasePath + "singletable/index.ftl";                     // 主页面模板路径
    private final static String formTplPath = GeneratorConfig.tplBasePath + "singletable/form.ftl";                      // 表单页模板路径
    // 生成 导入导出 excel 方法
    public static boolean hasExcel = true;
    private static String controllerOutPath = GeneratorConfig.outputBasePath + "controller/";             // controller 渲染文件输出路径
    private static String pageOutDirPath = GeneratorConfig.outputBasePath + "views/" + GeneratorConfig.moduleName + "/"; // 页面 输出文件输出目录


    /**
     * 重建输出路径
     * web 下用
     */
    public static void reBuildOutPath() {
        controllerOutPath = GeneratorConfig.outputBasePath + "controller/";
        pageOutDirPath = GeneratorConfig.outputBasePath + "views/" + GeneratorConfig.moduleName + "/";
    }

    public static Map<String, String> generate(List<TableMeta> tableMetas) throws IOException {
        log.debug("(*^▽^*) start generate singletable ");
        Map<String, String> ret = new HashMap<>();
        String controllerTplContent = FileUtils.readFileToString(new File(controllerTplPath), Constant.DEFAULT_ENCODEING);
        String indexTplContent = FileUtils.readFileToString(new File(indexTplPath), Constant.DEFAULT_ENCODEING);
        String formTplContent = FileUtils.readFileToString(new File(formTplPath), Constant.DEFAULT_ENCODEING);
        Map<String, Object> params;
        for (TableMeta tableMeta : tableMetas) {
            params = new HashMap<>();
            params.put("basePackageName", GeneratorConfig.basePackageName);
            params.put("moduleName", GeneratorConfig.moduleName);
            params.put("tableMeta", tableMeta);
            params.put("excludeFields", GeneratorConfig.excludeFields);
            params.put("author", GeneratorConfig.author);
            params.put("since", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            params.put("hasExcel", hasExcel);
            // controller
            ret.put(controllerOutPath + tableMeta.nameCamelFirstUp + "Controller.java", FreemarkerUtils.renderAsText(controllerTplContent, params));
            // index.ftl
            ret.put(pageOutDirPath + tableMeta.nameCamel + ".ftl", FreemarkerUtils.renderAsText(indexTplContent, params));
            // form.ftl
            ret.put(pageOutDirPath + tableMeta.nameCamel + "_form.ftl", FreemarkerUtils.renderAsText(formTplContent, params));
        }
        log.debug("(*^▽^*)  generate singletable over ");
        return ret;
    }

    public static void main(String[] rgs) throws IOException {
        MysqlMetaUtils utils = new MysqlMetaUtils(MysqlDataSourceUtils.getDataSource());
        List<TableMeta> tableMetas = utils.tableMetas(GeneratorConfig.schemaPattern, GeneratorConfig.tableNames, true);
        for (Map.Entry<String, String> entry : generate(tableMetas).entrySet()) {
            FileUtils.writeStringToFile(new File(entry.getKey()), entry.getValue(), Constant.DEFAULT_ENCODEING);
        }
    }
}

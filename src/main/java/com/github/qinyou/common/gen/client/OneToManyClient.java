package com.github.qinyou.common.gen.client;


import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.gen.GeneratorConfig;
import com.github.qinyou.common.gen.tools.MysqlDataSourceUtils;
import com.github.qinyou.common.gen.tools.MysqlMetaUtils;
import com.github.qinyou.common.gen.tools.TableMeta;
import com.github.qinyou.common.utils.freemarker.FreemarkerUtils;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 一对多表代码生成器
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class OneToManyClient {
    private final static String controllerTplPath = GeneratorConfig.tplBasePath + "oneToMany/controller.ftl"; // controller 模板文件路径
    private final static String indexTplPath = GeneratorConfig.tplBasePath + "oneToMany/index.ftl";           // 主页面模板路径
    private final static String formTplPath = GeneratorConfig.tplBasePath + "oneToMany/form.ftl";             // 表单页模板路径
    public static String mainTable;  // 主表
    public static Set<String> sonTables; // 子表
    public static String mainId;// 从表中 依赖字段名, 如果 被依赖非 主表ID, 需自行 修改生成的相关代码
    private static String controllerOutPath = GeneratorConfig.outputBasePath + "controller/";           // controller 渲染文件输出路径
    private static String pageOutDirPath = GeneratorConfig.outputBasePath + "views/" + GeneratorConfig.moduleName + "/"; // 页面 输出文件输出目录

    /**
     * 重建输出路径
     * web 下用
     */
    public static void reBuildOutPath() {
        controllerOutPath = GeneratorConfig.outputBasePath + "controller/";
        pageOutDirPath = GeneratorConfig.outputBasePath + "views/" + GeneratorConfig.moduleName + "/";
    }

    /**
     * 多表代码生成器
     *
     * @param mainTableMeta 主表表元信息
     * @param sonTableMetas 字表表元信息集合
     * @throws IOException 写文件异常
     */
    public static Map<String, String> generate(TableMeta mainTableMeta, List<TableMeta> sonTableMetas) throws IOException {
        log.debug("(*^▽^*) start generate oneToMany ");
        Map<String, String> ret = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("basePackageName", GeneratorConfig.basePackageName);
        params.put("moduleName", GeneratorConfig.moduleName);
        params.put("author", GeneratorConfig.author);
        params.put("since", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        params.put("generateDate", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        params.put("excludeFields", GeneratorConfig.excludeFields);
        params.put("mainTableMeta", mainTableMeta);
        params.put("sonTableMetas", sonTableMetas);
        params.put("mainId", mainId);
        params.put("mainIdCamel", StrKit.toCamelCase(mainId));

        // controller
        String tplContent = FileUtils.readFileToString(new File(controllerTplPath), Constant.DEFAULT_ENCODEING);
        ret.put(controllerOutPath + mainTableMeta.nameCamelFirstUp + "Controller.java", FreemarkerUtils.renderAsText(tplContent, params));
        // index.ftl
        tplContent = FileUtils.readFileToString(new File(indexTplPath), Constant.DEFAULT_ENCODEING);
        ret.put(pageOutDirPath + mainTableMeta.nameCamel + ".ftl", FreemarkerUtils.renderAsText(tplContent, params));
        // form.ftl
        tplContent = FileUtils.readFileToString(new File(formTplPath), Constant.DEFAULT_ENCODEING);
        ret.put(pageOutDirPath + mainTableMeta.nameCamel + "_form.ftl", FreemarkerUtils.renderAsText(tplContent, params));

        log.debug("(*^▽^*)  generate oneToMany over ");
        return ret;
    }

    public static void main(String[] args) throws IOException {
        MysqlMetaUtils utils = new MysqlMetaUtils(MysqlDataSourceUtils.getDataSource());
        Set<String> mainTableSet = new HashSet<>();
        mainTable = "ex_staff";
        mainTableSet.add(mainTable);
        List<TableMeta> mainTableMetas = utils.tableMetas(GeneratorConfig.schemaPattern, mainTableSet, true);
        TableMeta mainTableMeta = mainTableMetas.get(0);

        sonTables = new LinkedHashSet<>();
        sonTables.add("ex_staff_education");
        sonTables.add("ex_staff_experience");
        sonTables.add("ex_staff_family");

        mainId = "exStaffId";

        List<TableMeta> sonTableMetas = utils.tableMetas(GeneratorConfig.schemaPattern, sonTables, true);

        for (Map.Entry<String, String> entry : generate(mainTableMeta, sonTableMetas).entrySet()) {
            FileUtils.writeStringToFile(new File(entry.getKey()), entry.getValue(), Constant.DEFAULT_ENCODEING);
        }
    }
}

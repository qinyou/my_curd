package com.github.qinyou.common.gen.client;

import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.gen.Config;
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
 * model 代码生成器
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class ModelClient {

    private final static String baseModelTplPath = GeneratorConfig.tplBasePath + "model/baseModel.ftl"; // baseModel 模板文件路径
    private final static String modelTplPath = GeneratorConfig.tplBasePath + "model/model.ftl";         // Model 模板 路径
    public static boolean chainSetter = true;                                                    // 是否生成链式 setter 方法
    public static boolean hasExcel = false;                                                      // 是否生成 导出导出excel 所需要的注解
    private static String baseModelOutPath = GeneratorConfig.outputBasePath + "model/base/";      // baseModel 文件输出路径
    private static String modelOutPath = GeneratorConfig.outputBasePath + "model/";               // Model 文件输出路径


    /**
     * 重建输出路径
     * web 下用
     */
    public static void reBuildOutPath() {
        baseModelOutPath = GeneratorConfig.outputBasePath + "model/base/";
        modelOutPath = GeneratorConfig.outputBasePath + "model/";
    }


    /**
     * 生成baseModel、Model
     *
     * @param tableMetas
     * @return
     * @throws IOException
     */
    public static Map<String, String> generate(List<TableMeta> tableMetas) throws IOException {
        log.debug("(*^▽^*) start generate Model");
        Map<String, String> ret = new HashMap<>();

        String baseModelTpl = FileUtils.readFileToString(new File(baseModelTplPath), Constant.DEFAULT_ENCODEING);
        String modelTpl = FileUtils.readFileToString(new File(modelTplPath), Constant.DEFAULT_ENCODEING);
        Map<String, Object> params;
        for (TableMeta tableMeta : tableMetas) {
            params = new HashMap<>();
            params.put("basePackageName", GeneratorConfig.basePackageName);
            params.put("moduleName", GeneratorConfig.moduleName);
            params.put("getterTypeMap", Config.getterTypeMap);
            params.put("chainSetter", chainSetter);
            params.put("tableMeta", tableMeta);
            params.put("author", GeneratorConfig.author);
            params.put("since", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            params.put("hasExcel", hasExcel);
            ret.put(baseModelOutPath + "Base" + tableMeta.nameCamelFirstUp + ".java", FreemarkerUtils.renderAsText(baseModelTpl, params));
            ret.put(modelOutPath + tableMeta.nameCamelFirstUp + ".java", FreemarkerUtils.renderAsText(modelTpl, params));
        }
        log.debug("(*^▽^*) generate Model over");
        return ret;
    }

    public static void main(String[] args) throws IOException {
        MysqlMetaUtils utils = new MysqlMetaUtils(MysqlDataSourceUtils.getDataSource());
        List<TableMeta> tableMetas = utils.tableMetas(GeneratorConfig.schemaPattern, GeneratorConfig.tableNames, true);
        for (Map.Entry<String, String> entry : generate(tableMetas).entrySet()) {
            FileUtils.writeStringToFile(new File(entry.getKey()), entry.getValue(), Constant.DEFAULT_ENCODEING);
        }
    }
}

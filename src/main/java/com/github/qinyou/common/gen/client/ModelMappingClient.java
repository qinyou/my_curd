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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mappingkit 代码生成器.md
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class ModelMappingClient {

    private final static String mappingKitTplPath = GeneratorConfig.tplBasePath + "model/ModelMapping.ftl";   // 模板文件路径
    private static String mappingKitOutPath = GeneratorConfig.outputBasePath;                           // 渲染文件输出路径

    /**
     * 重建输出路径
     * web 下用
     */
    public static void reBuildOutPath() {
        mappingKitOutPath = GeneratorConfig.outputBasePath;
    }

    /**
     * 生成 ModelMapping
     *
     * @param tableMetas 表元数据集合
     * @throws IOException 文件读写异常
     */
    public static Map<String, String> generate(List<TableMeta> tableMetas) throws IOException {
        log.debug("(*^▽^*) start generate MappingKit");
        Map<String, String> ret = new HashMap<>();


        String tplContent = FileUtils.readFileToString(new File(mappingKitTplPath), Constant.DEFAULT_ENCODEING);
        Map<String, Object> params = new HashMap<>();
        params.put("basePackageName", GeneratorConfig.basePackageName);
        params.put("moduleName", GeneratorConfig.moduleName);
        params.put("tableMetas", tableMetas);
        params.put("author", GeneratorConfig.author);
        params.put("since", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        ret.put(mappingKitOutPath + StrKit.firstCharToUpperCase(GeneratorConfig.moduleName) + "ModelMapping.java", FreemarkerUtils.renderAsText(tplContent, params));
        log.debug("(*^▽^*) generate MappingKit over");
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

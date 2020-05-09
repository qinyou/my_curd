package com.github.qinyou.common.gen.client;

import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.gen.GeneratorConfig;
import com.github.qinyou.common.gen.tools.MysqlDataSourceUtils;
import com.github.qinyou.common.gen.tools.MysqlMetaUtils;
import com.github.qinyou.common.gen.tools.TableMeta;
import com.github.qinyou.common.utils.freemarker.FreemarkerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典（数据库表字典）生成器
 *
 * @author zhangchuang
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class ModelDictClient {
    private final static String dictTplPath = GeneratorConfig.tplBasePath + "model/dict.ftl";    // 模板路径
    public static boolean singleFile = false;                                           //  true ? 多张表一个文件:每张表一个文件
    private static String dictOutDirPath = GeneratorConfig.outputBasePath + "doc/model/";  // 输出目录


    /**
     * 重建输出路径
     * web 下用
     */
    public static void reBuildOutPath() {
        dictOutDirPath = GeneratorConfig.outputBasePath + "doc/model/";  // 输出目录
    }

    /**
     * 生成数据字典
     *
     * @param tableMetas
     * @return map, key是文件路径，value
     * @throws IOException
     */
    public static Map<String, String> generate(List<TableMeta> tableMetas) throws IOException {
        log.debug("(*^▽^*) start generate dict");
        Map<String, String> ret = new HashMap<>();

        String tplContent = FileUtils.readFileToString(new File(dictTplPath), Constant.DEFAULT_ENCODEING);  // 模板内容

        Map<String, Object> params;  // 渲染参数
        if (singleFile) {
            params = new HashMap<>();
            params.put("tableMetas", tableMetas);
            params.put("author", GeneratorConfig.author);
            ret.put(dictOutDirPath + "model_dict.html", FreemarkerUtils.renderAsText(tplContent, params));
        } else {
            List<TableMeta> tableMetasTemp;
            for (TableMeta tableMeta : tableMetas) {
                tableMetasTemp = new ArrayList<>();
                tableMetasTemp.add(tableMeta);
                params = new HashMap<>();
                params.put("tableMetas", tableMetasTemp);
                params.put("author", GeneratorConfig.author);
                ret.put(dictOutDirPath + tableMeta.name + ".html", FreemarkerUtils.renderAsText(tplContent, params));
            }
        }

        log.debug("(*^▽^*) generate dict over");
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

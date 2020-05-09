package com.github.qinyou.common.gen.client;

import com.github.qinyou.common.gen.tools.MysqlDataSourceUtils;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;

/**
 * jfinal官方 Model 代码生成器.md
 * maven 项目, oracle下存在bug，表字段如设置默认值 生成字典文件时报错 java.sql.SQLException: 流已被关闭
 */
public class JfinalModelClient {
    // base model 包名
    private static final String baseModelPkg = "com.github.qinyou.testModule.model.base";
    // base 路径
    //private static final String baseModelDir = PathKit.getWebRootPath() + "/src/main/java/com/mycurdpro/testModule/model/base";
    private static final String baseModelDir = "E:/codegen/src/main/java/com/github/qinyou/testModule/model/base";
    // model 包名
    private static final String modelPkg = "com.mycurdpro.testModule.model";
    // model 路径
    private static final String modelDir = baseModelDir + "/..";


    public static void main(String[] args) {
        Generator generator = new Generator(MysqlDataSourceUtils.getDataSource(), baseModelPkg, baseModelDir, modelPkg, modelDir);
        generator.setDialect(new MysqlDialect());
        // 链式
        generator.setGenerateChainSetter(true);
        // model 中生成 dao 对象
        generator.setGenerateDaoInModel(true);

        // 数据字典
        generator.setGenerateDataDictionary(true);
        generator.setDataDictionaryFileName("dict.txt");
        generator.setDataDictionaryOutputDir(baseModelDir); // base 包下
        generator.setGenerateRemarks(true); // baseModel 字段是否生成注释

        // 添加不需要生成的表名
        // generator.addExcludedTable("adv");
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        // generator.setRemovedTableNamePrefixes("t_");
        //generator.addExcludedTable("SYS_USER_BAK");

        generator.generate();
    }


}

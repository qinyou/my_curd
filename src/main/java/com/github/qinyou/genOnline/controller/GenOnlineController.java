package com.github.qinyou.genOnline.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.gen.GeneratorConfig;
import com.github.qinyou.common.gen.client.*;
import com.github.qinyou.common.gen.tools.ColumnMeta;
import com.github.qinyou.common.gen.tools.MysqlMetaUtils;
import com.github.qinyou.common.gen.tools.TableMeta;
import com.github.qinyou.common.render.ZipRender;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.web.BaseController;
import com.google.common.collect.Collections2;
import com.jfinal.plugin.activerecord.DbKit;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


/**
 * 在线代码生成器
 *
 * @author chuang
 */
@SuppressWarnings("Duplicates")
@RequirePermission("genOnline")
@Slf4j
public class GenOnlineController extends BaseController {

    private static MysqlMetaUtils metaUtils;

    /**
     * 首页
     */
    public void index() {
        render("genOnline/genOnline.ftl");
    }


    /**
     * datagrid 数据
     *
     * @throws SQLException
     */
    public void query() throws SQLException {
        DataSource dataSource =  DbKit.getConfig().getDataSource();

        if (dataSource == null) {
            renderDatagrid(new ArrayList<>());
            return;
        }
        if (metaUtils == null) {
            metaUtils = new MysqlMetaUtils(dataSource);
        }

        List<TableMeta> tableMetaList = metaUtils.allTableMeta(GeneratorConfig.schemaPattern);

        Collection<TableMeta> result = null;
        String tableName = getPara("extra_name");
        if (StringUtils.notEmpty(tableName)) {
            result = Collections2.filter(tableMetaList, x -> x.getName().contains(tableName));
            renderDatagrid(result, result.size());
        }

        String tableRemark = getPara("extra_remark");
        if (StringUtils.notEmpty(tableRemark)) {
            result = Collections2.filter(tableMetaList, x -> x.getRemark().contains(tableRemark));
            renderDatagrid(result, result.size());
        }

        if (result == null) {
            renderDatagrid(tableMetaList, tableMetaList.size());
        } else {
            renderDatagrid(result, result.size());
        }
    }


    /**
     * 代码生成器 表单
     */
    public void openGenForm() {
        String formName = get("formName");
        setAttr("tables", get("tables"));
        setAttr("basePackageName", GeneratorConfig.basePackageName);
        setAttr("moduleName", GeneratorConfig.moduleName);
        setAttr("author", GeneratorConfig.author);
        setAttr("hasExcel", ModelClient.hasExcel);
        setAttr("chainSetter", ModelClient.chainSetter);
        setAttr("singleFile", ModelDictClient.singleFile);
        render("genOnline/genOnline_" + formName + "_form.ftl");
    }


    /**
     * 生成 model
     */
    public void genModel() throws IOException {
        String tables = get("tables");
        if (StringUtils.isEmpty(tables)) {
            renderFail("tables 参数不可为空");
            return;
        }
        String basePackageName = get("basePackageName", GeneratorConfig.basePackageName);
        String moduleName = get("moduleName", GeneratorConfig.moduleName);
        String author = get("author", GeneratorConfig.author);
        String hasExcel = get("hasExcel", ModelClient.hasExcel ? "YES" : "NO");
        String chainSetter = get("chainSetter", ModelClient.chainSetter ? "YES" : "NO");

        GeneratorConfig.basePackageName = basePackageName;
        GeneratorConfig.moduleName = moduleName;
        GeneratorConfig.outputBasePath = GeneratorConfig.moduleName + "/";
        GeneratorConfig.author = author;
        ModelClient.hasExcel = "YES".equals(hasExcel);
        ModelClient.chainSetter = "YES".equals(chainSetter);
        SingleTableClient.hasExcel = "YES".equals(hasExcel);
        ModelClient.reBuildOutPath();
        ModelMappingClient.reBuildOutPath();

        List<String> fileNames = new ArrayList<>();
        List<String> fileContents = new ArrayList<>();

        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList(tables.split(",")));
        List<TableMeta> tableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, tableNames, true);

        ModelClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        ModelMappingClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        render(ZipRender.me().fileName(moduleName + "_model.zip").filenames(fileNames).data(fileContents));
    }

    /**
     * 生成 mappingKit
     */
    public void genMappingKit() throws IOException {
        String tables = get("tables");
        if (StringUtils.isEmpty(tables)) {
            renderFail("tables 参数不可为空");
            return;
        }
        String basePackageName = get("basePackageName", GeneratorConfig.basePackageName);
        String moduleName = get("moduleName", GeneratorConfig.moduleName);
        String author = get("author", GeneratorConfig.author);

        GeneratorConfig.basePackageName = basePackageName;
        GeneratorConfig.moduleName = moduleName;
        GeneratorConfig.outputBasePath = GeneratorConfig.moduleName + "/";
        GeneratorConfig.author = author;
        ModelMappingClient.reBuildOutPath();

        List<String> fileNames = new ArrayList<>();
        List<String> fileContents = new ArrayList<>();
        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList(tables.split(",")));
        List<TableMeta> tableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, tableNames, true);
        ModelMappingClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        render(ZipRender.me().fileName(moduleName + "_mapping.zip").filenames(fileNames).data(fileContents));
    }

    /**
     * 生成字典表
     */
    public void genModelDict() throws IOException {
        String tables = get("tables");
        if (StringUtils.isEmpty(tables)) {
            renderFail("tables 参数不可为空");
            return;
        }
        String basePackageName = get("basePackageName", GeneratorConfig.basePackageName);
        String moduleName = get("moduleName", GeneratorConfig.moduleName);
        String author = get("author", GeneratorConfig.author);
        String singleFile = get("singleFile", ModelDictClient.singleFile ? "YES" : "NO");

        GeneratorConfig.basePackageName = basePackageName;
        GeneratorConfig.moduleName = moduleName;
        GeneratorConfig.outputBasePath = GeneratorConfig.moduleName + "/";
        GeneratorConfig.author = author;
        ModelDictClient.singleFile = "YES".equals(singleFile);
        ModelDictClient.reBuildOutPath();

        List<String> fileNames = new ArrayList<>();
        List<String> fileContents = new ArrayList<>();
        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList(tables.split(",")));
        List<TableMeta> tableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, tableNames, true);

        ModelDictClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });

        render(ZipRender.me().fileName(moduleName + "_modeldict.zip").filenames(fileNames).data(fileContents));
    }

    /**
     * 生成 单表结构
     */
    public void genSingleTable() throws IOException {

        String tables = get("tables");
        if (StringUtils.isEmpty(tables)) {
            renderFail("tables 参数不可为空");
            return;
        }
        String basePackageName = get("basePackageName", GeneratorConfig.basePackageName);
        String moduleName = get("moduleName", GeneratorConfig.moduleName);
        String author = get("author", GeneratorConfig.author);
        String hasExcel = get("hasExcel", ModelClient.hasExcel ? "YES" : "NO");
        String chainSetter = get("chainSetter", ModelClient.chainSetter ? "YES" : "NO");

        GeneratorConfig.basePackageName = basePackageName;
        GeneratorConfig.moduleName = moduleName;
        GeneratorConfig.outputBasePath = GeneratorConfig.moduleName + "/";
        GeneratorConfig.author = author;
        ModelClient.hasExcel = "YES".equals(hasExcel);
        ModelClient.chainSetter = "YES".equals(chainSetter);
        SingleTableClient.hasExcel = "YES".equals(hasExcel);

        ModelClient.reBuildOutPath();
        ModelMappingClient.reBuildOutPath();
        ModelDictClient.reBuildOutPath();
        SingleTableClient.reBuildOutPath();

        List<String> fileNames = new ArrayList<>();
        List<String> fileContents = new ArrayList<>();

        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList(tables.split(",")));
        List<TableMeta> tableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, tableNames, true);

        ModelClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        ModelDictClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        ModelMappingClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        SingleTableClient.generate(tableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });

        render(ZipRender.me().fileName(moduleName + "_single.zip").filenames(fileNames).data(fileContents));
    }


    /**
     * 生成 一对多表 结构
     */
    public void genOneToMany() throws IOException {
        //  sonTable[]={ex_staff,ex_staff_education,ex_staff_experience}

        String mainId = get("mainId");
        if (StringUtils.isEmpty(mainId)) {
            renderFail("请输入 字表外键名");
            return;
        }

        String mainTable = get("mainTable");
        if (StringUtils.isEmpty(mainTable)) {
            renderFail("请选择 主表");
            return;
        }

        String[] sonTables = getParaValues("sonTables");
        if (sonTables == null || sonTables.length == 0) {
            renderFail("请至少选择 一个从表");
            return;
        }

        if (Arrays.asList(sonTables).contains(mainTable)) {
            renderFail(mainTable + " 不可同时为主从表");
            return;
        }

        // 子表外键名检查
        Set<String> sonTableSet = new LinkedHashSet<>();
        Collections.addAll(sonTableSet, sonTables);
        List<TableMeta> sonTableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, sonTableSet, true);
        List<ColumnMeta> columnMetas;
        boolean flag;
        for (TableMeta tableMeta : sonTableMetas) {
            columnMetas = tableMeta.getColumnMetas();
            flag = false;
            for (ColumnMeta columnMeta : columnMetas) {
                if (columnMeta.getName().equals(mainId)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                renderFail(tableMeta.getName() + "表 不包含字段 " + mainId + ", 请确认 子表外键名");
                return;
            }
        }


        String basePackageName = get("basePackageName", GeneratorConfig.basePackageName);
        String moduleName = get("moduleName", GeneratorConfig.moduleName);
        String author = get("author", GeneratorConfig.author);
        String chainSetter = get("chainSetter", ModelClient.chainSetter ? "YES" : "NO");

        GeneratorConfig.basePackageName = basePackageName;
        GeneratorConfig.moduleName = moduleName;
        GeneratorConfig.outputBasePath = GeneratorConfig.moduleName + "/";
        GeneratorConfig.author = author;
        ModelClient.chainSetter = "YES".equals(chainSetter);
        OneToManyClient.sonTables = sonTableSet;
        OneToManyClient.mainTable = mainTable;
        OneToManyClient.mainId = mainId;

        ModelClient.reBuildOutPath();
        ModelMappingClient.reBuildOutPath();
        ModelDictClient.reBuildOutPath();
        OneToManyClient.reBuildOutPath();

        Set<String> mainTableSet = new HashSet<>();
        mainTableSet.add(mainTable);
        List<TableMeta> mainTableMetas = metaUtils.tableMetas(GeneratorConfig.schemaPattern, mainTableSet, true);
        TableMeta mainTableMeta = mainTableMetas.get(0);

        List<String> fileNames = new ArrayList<>();
        List<String> fileContents = new ArrayList<>();

        OneToManyClient.generate(mainTableMeta, sonTableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });

        sonTableMetas.add(mainTableMeta);
        ModelClient.generate(sonTableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        ModelDictClient.generate(sonTableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        ModelMappingClient.generate(sonTableMetas).forEach((k, v) -> {
            fileNames.add(k);
            fileContents.add(v);
        });
        render(ZipRender.me().fileName(moduleName + "_oneToMany.zip").filenames(fileNames).data(fileContents));
    }
}

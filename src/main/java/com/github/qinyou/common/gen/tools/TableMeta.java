package com.github.qinyou.common.gen.tools;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.qinyou.common.gen.Config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TableMeta, 用于代码生成器
 */
@SuppressWarnings("unused")
public class TableMeta implements Serializable {

    private static final long serialVersionUID = -934868752674285833L;

    // 表名
    @JSONField(ordinal = 1)
    public String name;

    // 表名 驼峰
    @JSONField(ordinal = 2)
    public String nameCamel;

    // 表名 驼峰 首字大写
    @JSONField(ordinal = 3)
    public String nameCamelFirstUp;

    // 表 备注
    @JSONField(ordinal = 4)
    public String remark;

    // 表 主键
    @JSONField(ordinal = 5)
    public List<String> primaryKeys;

    //表列集合
    @JSONField(ordinal = 6)
    public List<ColumnMeta> columnMetas;

    // base model 中必须 import 的包
    @JSONField(ordinal = 7)
    private Set<String> necessaryImport = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCamel() {
        return nameCamel;
    }

    public void setNameCamel(String nameCamel) {
        this.nameCamel = nameCamel;
    }

    public String getNameCamelFirstUp() {
        return nameCamelFirstUp;
    }

    public void setNameCamelFirstUp(String nameCamelFirstUp) {
        this.nameCamelFirstUp = nameCamelFirstUp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<ColumnMeta> getColumnMetas() {
        return columnMetas;
    }

    public void setColumnMetas(List<ColumnMeta> columnMetas) {
        this.columnMetas = columnMetas;
        for (ColumnMeta columnMeta : columnMetas) {
            if (columnMeta.getJavaTypeShortName() == null || Config.excludeImportTypes.contains(columnMeta.getJavaTypeShortName())) {
                continue;
            }
            this.necessaryImport.add(columnMeta.javaType);
        }
    }

    public Set<String> getNecessaryImport() {
        return necessaryImport;
    }

    public void setNecessaryImport(Set<String> necessaryImport) {
        this.necessaryImport = necessaryImport;
    }
}

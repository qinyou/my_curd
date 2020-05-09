<#if (tableMetas)??>
  <#list tableMetas as tableMeta>
### ${(tableMeta.name)!}   ${(tableMeta.remark)!} 
| No.  | Field  | Type  | Nullable  | Key | Default | Remarks |
| :------------: | ------------ | ------------ | :------------: | ------------ | ------------ | ------------ |
    <#if (tableMeta.columnMetas)??>
        <#list tableMeta.columnMetas as columnMeta>
| ${columnMeta?counter} | ${(columnMeta.name)!} |  ${(columnMeta.dbType)!}(${(columnMeta.size)!}<#if (columnMeta.decimalDigits) gt 0>,${(columnMeta.decimalDigits)!}</#if>) | ${(columnMeta.nullable)?string("✔", "✘")}  | ${(columnMeta.primaryKey)?string("PRI", "")}  | ${(columnMeta.defaultValue)!}  | ${(columnMeta.remark)!}  |
        </#list>
    </#if>
    <#if tableMeta_has_next>
------------
    </#if>
  </#list>
</#if>



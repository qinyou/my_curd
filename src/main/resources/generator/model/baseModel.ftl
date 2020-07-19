package ${(basePackageName)!}.${(moduleName)!}.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;
<#if hasExcel>import cn.afterturn.easypoi.excel.annotation.Excel;</#if>
<#-- 必须引入的包 -->
<#if (tableMeta.necessaryImport)??>
    <#list tableMeta.necessaryImport as necessaryImport>
import ${(necessaryImport)!};
    </#list>
</#if>
/**
 * Generated baseModel
 * DB table: ${(tableMeta.name)!}  ${(tableMeta.remark)!}
 * @author ${(author)!'Generator'}
 * @since ${(since)!}
 */
@SuppressWarnings({"serial", "unchecked", "Duplicates"})
public abstract class Base${(tableMeta.nameCamelFirstUp)!}<M extends Base${(tableMeta.nameCamelFirstUp)!}<M>> extends Model<M> implements IBean {
 <#if hasExcel>// --- 导出导出excel 所需-----
<#if (tableMeta.columnMetas)??>
    <#list tableMeta.columnMetas as column>
        <#if !(column.primaryKey) >
    @Excel(name = "<#if (column.remark)?? && column.remark != "">${(column.remark)!}<#else>${(column.name)!}</#if>", height = 10, width = 30)
    private ${(column.javaTypeShortName)!} ${(column.nameCamel)!};
        </#if>
    </#list>
</#if>
    //--- 导出导出excel 所需-----</#if>

    <#-- get set 方法 -->
<#if (tableMeta.columnMetas)??>
    <#list tableMeta.columnMetas as column>

        <#if (getterTypeMap["${(column.javaTypeShortName)!}"])?? >
            <#assign getterOfModel = getterTypeMap["${(column.javaTypeShortName)!}"] >
        <#else>
            <#assign getterOfModel = 'get'>
        </#if>

     // ${(column.remark)!}
     public ${(column.javaTypeShortName)!} get${(column.nameCamelFirstUp)!}() {
        return ${(getterOfModel)!}("${(column.name)!}");
     }

     public ${chainSetter?string( 'M','void')} set${(column.nameCamelFirstUp)!}(${(column.javaTypeShortName)!} ${(column.nameCamel)!}) {
        set("${(column.name)!}", ${(column.nameCamel)!});
        <#if chainSetter >
        return (M)this;
        </#if>
     }
    </#list>
</#if>
}

package ${(basePackageName)!}.${(moduleName)!};

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import ${(basePackageName)!}.${(moduleName)!}.model.*;

/**
 * Generated MappingKit
 * ${(moduleName)!} 模块数据表  MappingKit
 * @author ${(author)!'Generator'}
 * @since ${(since)!}
 */
public class ${(moduleName)?cap_first}ModelMapping {

	public static void mapping(ActiveRecordPlugin arp) {
<#if (tableMetas)??>
    <#list tableMetas as tableMeta>
        // ${(tableMeta.remark)!}
		arp.addMapping("${(tableMeta.name)!}", "${(tableMeta.primaryKeys)?join(",")}", ${(tableMeta.nameCamelFirstUp)!}.class);
    </#list>
</#if>
	}
}


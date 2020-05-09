<#noparse><#-- </#noparse> ${author!} ${since!} <#noparse>--></#noparse>
<#noparse><#include "../common/common.ftl"/>
<@layout></#noparse>
<table id="dg" class="easyui-datagrid"
       url="<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <#list tableMeta.columnMetas as col>
            <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) >
        <th field="${(col.name)!}" width="150"><#if (col.remark)?? && col.remark != "">${(col.remark)!}<#else>${(col.name)!}</#if></th>
            </#if>
        </#list>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/newModel', '500px', '400px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/newModel', '500px', '400px')" href="#"
       class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/deleteAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>

<#if hasExcel>
   <a onclick="goUploadPage('<#noparse>${ctx!}</#noparse>/utils/goUploadFilePage','<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/importExcel','上传excel','导入excel')"
      href="#" class="easyui-linkbutton"   iconCls="iconfont icon-import" plain="true">导入</a>
   <a onclick="exportExcel('<#noparse>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/exportExcel','searchSpan')" href="#" class="easyui-linkbutton"
     iconCls="iconfont icon-export" plain="true">导出</a>
</#if>

    <span id="searchSpan" class="searchInputArea">
            <input name="search_LIKE_test" prompt="测试" class="easyui-textbox" style="width:120px; ">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="<#noparse>${ctx!}</#noparse>/static/js/dg-curd.js"></script>
<#noparse>
</@layout>
</#noparse>

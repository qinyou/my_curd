<#noparse><#-- </#noparse> ${author!} ${generateDate!} <#noparse>--></#noparse>
<#noparse><#include "../common/common.ftl"/>
<@layout></#noparse>
<table id="dg" class="easyui-datagrid"
       url="<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'ID',checkbox:true"></th>
        <#list mainTableMeta.columnMetas as col>
            <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) >
        <th field="${(col.name)!}" width="150"><#if (col.remark)?? && col.remark != "">${(col.remark)!}<#else>${(col.name)!}</#if></th>
            </#if>
        </#list>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/newModel','800px', '500px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/newModel','800px', '500px')" href="#"
       class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/deleteAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>
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

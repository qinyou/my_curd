<#--  zhangchuang 2019-06-15 20:24:55 -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/sysSetting/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"  nowrap="false"
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th field="settingInfo" width="250">说明</th>
        <th field="settingCode" width="250">设置编码</th>
        <th field="settingValue" width="450">设置编码值</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/sysSetting/newModel', '350px', '400px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/sysSetting/newModel', '350px', '400px')" href="#"
       class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/sysSetting/deleteAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>

   <a onclick="goUploadPage('${ctx!}/utils/goUploadFilePage','${ctx!}/sysSetting/importExcel','上传excel','导入excel')"
      href="#" class="easyui-linkbutton"   iconCls="iconfont icon-import" plain="true">导入</a>
   <a onclick="exportExcel('${ctx!}/sysSetting/exportExcel','searchSpan')" href="#" class="easyui-linkbutton"
     iconCls="iconfont icon-export" plain="true">导出</a>

    <span id="searchSpan" class="searchInputArea">
            <input name="search_LIKE_test" prompt="测试" class="easyui-textbox" style="width:120px; ">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

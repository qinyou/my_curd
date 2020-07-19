<#--  chuang 2020-06-30 16:48:40 -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/actFormTpl/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th field="formKey" width="150">表单key</th>
        <th field="formVersion" width="150">版本</th>
        <th field="remark" width="250">描述</th>
        <th field="plugins" width="250">插件</th>
        <th field="state" width="150" formatter="stateFmt">状态</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/actFormTpl/newModel', '700px', '460px')"  class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/actFormTpl/newModel', '800px', '560px')"
       class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/actFormTpl/deleteAction')"  class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>
    <a onclick="viewModel('表单预览','dg','${ctx!}/actFormTpl/viewModel','840px')"  class="easyui-linkbutton  "
       iconCls="iconfont icon-look" plain="true">预览</a>
    <a onclick="downloadHtml()"  class="easyui-linkbutton" iconCls="iconfont icon-download" plain="true">下载</a>
    <span id="searchSpan" class="searchInputArea">
            <input name="search_LIKE_formKey" prompt="表单key" class="easyui-textbox" style="width:150px; ">
            <input name="search_LIKE_remark" prompt="描述" class="easyui-textbox" style="width:150px; ">
            <input name="search_EQ_state" prompt="状态"  class="easyui-combobox"  style="width:120px; "
                 data-options="data: [{value:'1',text:'启用'} ,{value:'0',text:'禁用'}], panelHeight:'auto',value:''">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function downloadHtml(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length ===1) {
            window.open("${ctx!}/actFormTpl/downloadHtml?id="+rows[0].id);
        } else {
            popup.msg('请选择一行数据下载模板文件');
        }
    }
</script>
</@layout>

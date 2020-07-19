<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/processDeploy/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"x
       striped="false"  pagination="true"
       data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
       }"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th field="id" width="150">ID</th>
        <th field="name" width="350">部署名</th>
        <th field="date" width="250">部署时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/processDeploy/deployModel', '450px', '300px','部署')"  class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>

    <a onclick="deleteModel('dg','${ctx!}/processDeploy/unDeployAction','卸载')"  class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">卸载</a>

    <a onclick="downZip()"  class="easyui-linkbutton  "
       iconCls="iconfont icon-download" plain="true">下载</a>

    <span id="searchSpan" class="searchInputArea">
          <input name="extra_id" prompt="ID" class="easyui-textbox" style="width:150px; ">
          <input name="extra_name" prompt="部署名" class="easyui-textbox" style="width:150px; ">
          <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
<script>
    function downZip(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length===1) {
            window.open("${ctx!}/processDeploy/downloadZip?id="+rows[0].id);
        } else {
            popup.msg('请选择一行数据下载部署包');
        }
    }
</script>
</@layout>

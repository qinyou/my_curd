<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/processDeploy/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"x
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th field="id" width="150">部署Id</th>
        <th field="deploymentTime" width="250">部署时间</th>
        <th field="category" width="200">分类</th>
        <th field="name" width="350">备注</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/processDeploy/deployModel', '450px', '300px','部署')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">部署</a>

    <a onclick="deleteModel('dg','${ctx!}/processDeploy/unDeployAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">卸载</a>

    <a onclick="downZip()" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-download" plain="true">下载</a>

    <span id="searchSpan" class="searchInputArea">
          <input name="extra_category" prompt="分类" class="easyui-combobox"  style="width:120px; "
               data-options="panelHeight:'auto',valueField:'value',textField:'label',
               url:'${ctx!}/sysDict/combobox?groupCode=OACategory'">
          <input name="extra_name" prompt="备注" class="easyui-textbox" style="width:120px; ">

          <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
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

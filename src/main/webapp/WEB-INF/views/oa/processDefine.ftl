<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/processDefine/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"x
       striped="false"  pagination="true"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th field="dg" width="250" formatter="dgFmt">Id</th>
        <th field="key" width="150">key</th>
        <th field="version" width="100">版本号</th>
        <th field="name" width="150">名称</th>
        <th field="statue" width="100">状态</th>
        <th field="description" width="250">描述</th>
        <th field="category" width="250">命名空间</th>
        <th field="deploymentId" width="250">部署Id</th>
    </tr>
    </thead>
</table>
<div id="tb">

    <a onclick="deleteModel('dg','${ctx!}/processDefine/activateProcessDefine', '激活')"  class="easyui-linkbutton"
    iconCls="iconfont icon-add" plain="true">激活</a>

    <a onclick="deleteModel('dg','${ctx!}/processDefine/suspendProcessDefine','挂起')" href="#" class="easyui-linkbutton  "
    iconCls="iconfont icon-delete" plain="true">挂起</a>

    <span id="searchSpan" class="searchInputArea">
          <input name="extra_key" prompt="key" class="easyui-textbox" style="width:120px; ">

          <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
    <script>
        function dgFmt(val,row,index){
            return '<a target="_blank" href="${ctx!}/oa/definitionDiagram?id='+row.id+'">'+row.id+'</a>'
        }
    </script>
</@layout>

<#include "../common/common.ftl"/>
<#include "../common/btnControl.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/processInstance/query"
           data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
           }"
           toolbar="#tb"  rownumbers="true" border="false" singleSelect="true"
           fit="true" fitColumns="false"
           striped="false" pagination="true"
           pageSize="25" pageList="[25,40]" >
        <thead>
        <tr>
            <th hidden field="definitionId" width="200">定义Id</th>
            <th hidden field="definitionKey" width="200">定义Key</th>
            <th field="instanceId" width="100">流程ID</th>
            <th field="instanceName" width="350" formatter="fmtAsDetailLink">流程</th>
            <th field="startUser" width="100" formatter="usernameFmt">申请人</th>
            <th field="startTime" width="200">发起时间</th>
            <th field="state"  width="300" align="center" formatter="fmtAsCurrentLink">状态</th>
            <#--更多的超级管理权限 按钮-->
        </tr>
        </thead>
    </table>
    <div id="tb">
        <div id="searchSpan" class="searchInputAreaDiv"  >
            <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:150px;">
            <input name="extra_instanceName" prompt="流程名" class="easyui-textbox" style="width:200px;">
            <input name="extra_applyUser" prompt="申请人" class="easyui-textbox" style="width:150px;">
            <input name="extra_finished" prompt="状态" class="easyui-combobox"  style="width:120px; "
                   data-options="data: [{value:'true',text:'已结束'} ,{value:'false',text:'正运行'}], panelHeight:'auto',value:''">
            <span class="blankSep"></span>
            <input name="extra_definitionId" prompt="流程定义ID" class="easyui-textbox" style="width:180px;">
            <input name="extra_definitionKey" prompt="流程定义KEY" class="easyui-textbox" style="width:180px;">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/process.js"></script>
</@layout>

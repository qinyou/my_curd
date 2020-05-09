<#include "../common/common.ftl"/>
<#include "../common/btnControl.ftl"/>
<@layout>

<div class="easyui-tabs" fit="true" border="false" tabPosition="left"  pill="true" narrow="false" plain="true" >
    <div title="运行时" fit="true"  >
        <table id="dg" class="easyui-datagrid"
               url="${ctx!}/processInstance/query?finished=false"
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
                <th hidden="true" field="processDefinitionId" width="200">定义Id</th>
                <th hidden="true" field="processDefinitionKey" width="200">定义Key</th>
                <th hidden="true" field="businessKey" width="200">业务表Id</th>

                <th field="processInstanceId" width="100">流程ID</th>
                <th field="startUserId" width="100" formatter="usernameFmt">申请人</th>
                <th hidden="true"  field="startTime" width="200">申请时间</th>
                <th field="name" width="350" formatter="runningDetailFmt">流程</th>

                <th  field="localizedDescription" width="200" formatter="highlightFmt" >当前节点</th>
            </tr>
            </thead>
        </table>
        <div id="tb">
            <div id="searchSpan" class="searchInputAreaDiv"  style="text-align: left;padding: 10px 20px;" >
                <input name="extra_applyUser" prompt="申请人" class="easyui-textbox" style="width:180px;">
                <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_instanceName" prompt="流程名" class="easyui-textbox" style="width:180px;">
                <input name="extra_definitionId" prompt="定义ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_definitionKey" prompt="定义ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_businessKey" prompt="业务表ID" class="easyui-textbox" style="width:180px;">
                <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg','searchSpan')">搜索</a>
            </div>
        </div>
    </div>
    <div title="已结束" fit="true">
        <table id="dg2" class="easyui-datagrid"
               url="${ctx!}/processInstance/query?finished=true"
               data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
               },rowStyler: function(index,row){
					if (row.deleteReason != null){
						return 'background-color:#e2e0e0';
					}
				}"
               toolbar="#tb2"  rownumbers="true" border="false" singleSelect="true"
               fit="true" fitColumns="false"
               striped="false" pagination="true"
               pageSize="25" pageList="[25,40,50]" >
            <thead>
            <tr>
                <th hidden="true" field="processDefinitionId" width="200">定义Id</th>
                <th hidden="true" field="processDefinitionKey" width="200">定义Key</th>
                <th hidden="true" field="businessKey" width="200">业务表Id</th>

                <th field="processInstanceId" width="100">流程ID</th>
                <th field="startUserId" width="100" formatter="usernameFmt">申请人</th>
                <th field="name" width="350" formatter="finishedDetailFmt">流程</th>

                <th  field="startTime" width="200">开始时间</th>
                <th  field="endTime" width="200">结束时间</th>

            </tr>
            </thead>
        </table>
        <div id="tb2">
            <div id="searchSpan" class="searchInputAreaDiv" style="text-align: left;padding: 10px 20px;"  >
                <input name="extra_applyUser" prompt="申请人" class="easyui-textbox" style="width:180px;">
                <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_instanceName" prompt="流程名" class="easyui-textbox" style="width:180px;">
                <input name="extra_definitionId" prompt="定义ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_definitionKey" prompt="定义ID" class="easyui-textbox" style="width:180px;">
                <input name="extra_businessKey" prompt="业务表ID" class="easyui-textbox" style="width:180px;">
                <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg2','searchSpan')">搜索</a>
            </div>
        </div>
    </div>
</div>

    <script src="${ctx!}/static/plugins/easyui1.8.5/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/oa.js"></script>
    <script>
        function runningDetailFmt(val,row) {
            return  '<a href="javascript:openProcessInstanceDetail(true,\''+row.processInstanceId+'\',\'流程详情\')">'+val+'</a>';
        }
        function finishedDetailFmt(val,row) {
            return  '<a href="javascript:openProcessInstanceDetail(false,\''+row.processInstanceId+'\',\'流程详情\')">'+val+'</a>';
        }
    </script>
</@layout>

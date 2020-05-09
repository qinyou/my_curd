<#include "../common/common.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myCompleteTask/query"
           data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
           }"
           toolbar="#tb" rownumbers="true" border="false"
           fit="true"    fitColumns="false" nowrap="false"
           striped="false"  pagination="true" singleSelect="true"
           pageSize="40" pageList="[30,40,50]">
        <thead>
        <tr>
            <th  field="processInstanceId" width="100" >ID</th>
            <th field="initiator" width="100" formatter="usernameFmt">申请人</th>
            <th field="name" width="300" formatter="processNameFmt">流程</th>
            <th field="statue" width="200" formatter="statueFmt">流程状态</th>
            <th  field="startTime" width="200">开始时间</th>
            <th  field="endTime" width="150">结束时间</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <div id="searchSpan" class="searchInputAreaDiv"  style="text-align: left; padding: 10px 0 10px 50px">
            <input name="extra_instanceId" prompt="流程Id" class="easyui-textbox" style="width:150px; ">
            <span style="display: inline-block;width: 20px;"></span>
            <input name="extra_applyUser" prompt="发起人" class="easyui-textbox" style="width:150px; ">
            <span style="display: inline-block;width: 20px;"></span>
            <input name="extra_processName" prompt="流程名" class="easyui-textbox" style="width:180px;">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/easyui1.8.5/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/oa.js"></script>
</@layout>

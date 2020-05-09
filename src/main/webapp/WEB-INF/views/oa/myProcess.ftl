<#include "../common/common.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myProcess/query"
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
            <!--流程实例-->
            <th  field="processInstanceId" width="100" >流程Id</th>
            <th  field="name" width="300" formatter="processNameFmt">流程</th>
            <th  field="statue" width="200" formatter="statueFmt">流程状态</th>
            <th  field="startTime" width="200">开始时间</th>
            <th  field="endTime" width="150">结束时间</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <a onclick="newModel('${ctx!}/myProcess/newProcess', '800px', '500px','新建流程')" href="#" class="easyui-linkbutton"
           iconCls="iconfont icon-add" plain="true">新建申请</a>
        <a onclick="deleteProcess()" href="#" class="easyui-linkbutton  "
           iconCls="iconfont icon-delete" plain="true">删除作废</a>

        <span id="searchSpan" class="searchInputArea"  >
             <input name="extra_instanceId" prompt="流程Id" class="easyui-textbox" style="width:180px;">
            <input name="extra_processName" prompt="流程" class="easyui-textbox" style="width:180px;">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </span>
    </div>
    <script src="${ctx!}/static/plugins/easyui1.8.5/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script>
        function deleteProcess() {
            var row = $("#dg").datagrid("getSelected");
            if (row!=null) {
                popup.openConfirm(null,3, '作废确认', '您确定删除作废选中的流程吗?', function () {
                    var businessKey = row.businessKey;
                    var businessForm = row.businessForm;
                    if(isEmpty(businessForm) || isEmpty(businessForm)){
                        popup.msg('businessKey 或 businessForm 参数缺失，操作失败 ');
                        return;
                    }
                    $.post('${ctx!}/myProcess/deleteProcess?businessKey='+businessKey+"&formKey="+businessForm, function (data) {
                        if(data.state==='ok'){
                            popup.msg(data.msg, function () {
                                $('#dg').datagrid('reload');
                            });
                        }else if(data.state==='error'){
                            popup.errMsg('系统异常',data.msg);
                        }else{
                            popup.msg(data.msg);
                        }
                    }, "json").error(function(){ popup.errMsg(); });
                });
            } else {
                popup.msg('请选择一行进行操作');
            }
        }
    </script>
    <script src="${ctx!}/static/js/oa.js"></script>
</@layout>

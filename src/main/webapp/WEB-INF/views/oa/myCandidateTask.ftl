<#include "../common/common.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myCandidateTask/query"
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
           pageSize="40" pageList="[30,40,50]" >
        <thead>
        <tr>
            <th field="processInstanceId" width="100">流程ID</th>
            <th field="initiator" width="100" formatter="usernameFmt">申请人</th>
            <th field="processInstanceName" width="350" formatter="processInstanceDetailFmt">流程</th>
            <th field="taskName" width="150" formatter="highlightFmt">任务节点</th>
            <!--任务创建时间-->
            <th  hidden="true" field="createTime" width="200">任务时间</th>
            <th field="type" width="250" formatter="opeFmt">操作</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <div id="searchSpan" class="searchInputAreaDiv"  >
             <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:180px;">
             <input name="extra_taskName" prompt="任务名" class="easyui-textbox" style="width:180px;">
             <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/easyui1.8.5/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/oa.js"></script>
    <script>
        function processInstanceDetailFmt(val,row) {
            return  '<a href="javascript:openProcessInstanceDetail(true,\''+row.processInstanceId+'\',\'流程详情\')">'+val+'</a>';
        }

        /*认领任务*/
        function claimAction(id) {
            $.get('${ctx!}/myCandidateTask/claimAction?id=' + id, function (data) {
                if (data.state === 'ok') {
                    popup.msg(data.msg, function () {
                        $('#dg').datagrid('reload');
                    });
                } else if (data.state === 'error') {
                    popup.errMsg('系统异常', data.msg);
                } else {
                    popup.msg(data.msg);
                }
            }, "json").error(function () {
                popup.errMsg();
            });
        }

        /*操作按钮格式化*/
        function opeFmt(val, row) {
            return '<a  href="javascript:claimAction(\'' + row.id + '\')"> [认领] </a>';
        }
    </script>
</@layout>

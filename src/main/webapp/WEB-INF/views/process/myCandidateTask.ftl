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
            <th field="taskName" align="center" width="150">任务名</th>
            <th field="taskCreateTime" width="200"><#--任务-->开始时间</th>
            <th field="instanceId" width="100">流程ID</th>
            <th field="instanceName" width="400" formatter="fmtAsDetailLink">流程</th>
            <th field="ope" width="250" formatter="opeFmt">操作</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <div id="searchSpan" class="searchInputAreaDiv"  >
            <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:180px;">
            <span class="blankSep"></span>
            <input name="extra_taskName" prompt="任务名" class="easyui-textbox" style="width:180px;">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/process.js"></script>
    <script>
        /*认领任务*/
        function claimAction(id) {
            $.get('${ctx!}/myCandidateTask/claimAction?taskId=' + id, function (data) {
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
            return '<button class="pure-button pure-button-primary button-xsmall"  onclick="claimAction(\'' + row.id + '\')"> 认领</button>';
        }
    </script>
</@layout>

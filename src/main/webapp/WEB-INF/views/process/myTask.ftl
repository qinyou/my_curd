<#include "../common/common.ftl"/>
<#include "../common/btnControl.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myTask/query"
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
            <th field="createTime" width="200"><#--任务-->创建时间</th>
            <th field="instanceId" width="100">流程ID</th>
            <th field="instanceName" width="400" formatter="fmtAsDetailLink">流程</th>
            <th field="ope" align="center" width="100" formatter="opeFmt">办理</th>
            <@hasBtnCode "myTask:changeAssignee">
            <th field="ope2" align="center"  width="100" formatter="ope2Fmt">转办</th>
            </@hasBtnCode>
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
        function opeFmt(val, row) {
            return  '<button class="pure-button button-xsmall pure-button-primary"  onclick="goCompleteForm(\'' + row.id + '\')">办理</button>';
        }
        function goCompleteForm(id) {
            /* 打开的窗口页面可能要全局刷新（调整表单），所以此处追加 window.name 参数，办理完成后 才可以正确刷新表格*/
            popup.openIframe('任务办理', '${ctx!}/myTask/goCompleteForm?taskId=' + id+'&winName='+window.name, "800px", "96%");
        }


        <@hasBtnCode "myTask:changeAssignee">
        /*转办按钮*/
        function ope2Fmt(val, row) {
            return  '<button class="pure-button button-xsmall"  onclick="openUtilsUser(true,\'转办\')">转办</button>';
        }
        </@hasBtnCode>

        /*任务转办，只所以叫 addUsersAction, 是选择用户 弹窗是个通用方法*/
        function addUsersAction(userInfoAry){
            if(userInfoAry.length===0 || userInfoAry.length>1){
                popup.msg('必须且仅可以选择一个用户');
                return;
            }
            var username = userInfoAry[0].username;
            var row = $("#dg").datagrid("getSelected");
            if(row==null){
                popup.msg('代办任务行未选中');
                return;
            }
            var taskId = row.id;
            $.post('${ctx!}/myTask/changeAssigneeAction?taskId=' + taskId+"&username="+username, function (data) {
                if(data.state==='ok'){
                    popup.msg(data.msg, function () {
                        $('#dg').datagrid('reload');
                        popup.closeByIndex(winIndex);
                    });
                }else if(data.state==='error'){
                    popup.errMsg('系统异常',data.msg);
                }else{
                    popup.msg(data.msg);
                }
            }, "json").error(function(){ popup.errMsg();});
        }
    </script>
</@layout>

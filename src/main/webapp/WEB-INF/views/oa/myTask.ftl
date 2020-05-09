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
            <th field="processInstanceId" width="100">流程ID</th>
            <th field="initiator" width="100" formatter="usernameFmt">申请人</th>
            <th field="processInstanceName" width="350" formatter="processInstanceDetailFmt">流程</th>
            <th field="taskName" width="150" formatter="highlightFmt">任务节点</th>
            <!--任务创建时间-->
            <th  hidden="true" field="createTime" width="200">任务时间</th>
            <th field="type" width="80" formatter="opeFmt">操作1</th>
            <@hasBtnCode "myTask:changeAssignee">
            <th field="type2" width="80" formatter="ope2Fmt">操作2</th>
            </@hasBtnCode>
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

        /*打开办理表单*/
        function openCompleteForm(id, title) {
            popup.openIframe(title || '任务办理', '${ctx!}/myTask/goCompleteForm?id=' + id, "1000px", "96%");
        }


        /*办理按钮*/
        function opeFmt(val, row) {
            var txt =  '<a  href="javascript:openCompleteForm(\'' + row.id + '\')"> [办理] </a>';
            return txt;
        }

        <@hasBtnCode "myTask:changeAssignee">
        /*转办按钮*/
        function ope2Fmt(val, row) {
            if(row.taskDefinitionKey !== 'adjustForm'){
                txt =  '<a  href="javascript:openUtilsUser(true,\'转办\')"> [转办] </a>';
            }else{
                txt='无';
            }
            return txt;
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

<#include "../common/common.ftl"/>
<#include "../common/btnControl.ftl"/>
<@layout>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myApply/query"
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
            <th field="instanceId" width="100">流程ID</th>
            <th field="instanceName" width="400" formatter="fmtAsDetailLink">流程</th>
            <th field="startTime"  width="150">申请时间</th>
            <th field="state" align="center" width="300" formatter="fmtAsCurrentLink">状态</th>
            <#if (session.buttonCodes)?seq_contains('myApply:cancel') || (session.buttonCodes)?seq_contains('myApply:cancel')  >
                <th field="ope" align="center"  width="200" formatter="opeFmt">操作</th>
            </#if>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <#--'${ctx!}/myApply/newApply?processKey=holiday-request&test=test-->
        <a onclick="newModel('${ctx!}/myApply/goCategoryList', '900px', '80%','新建申请')"  class="easyui-linkbutton"
           iconCls="iconfont icon-add" plain="true">新建申请</a>
        <span id="searchSpan" class="searchInputArea">
        <input prompt="流程分类"  class="easyui-combobox" name="extra_definitionKey" style="width:200px;" data-options="
                url: '${ctx!}/myApply/listCate', panelHeight: 'auto',
                valueField:'processKey', textField:'name', groupField:'pName',
                formatter: function(row){
                       return row.name + '('+row.c+')';
                }">
        <span class="blankSep"></span>
        <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:150px;">
        <input name="extra_instanceName"   prompt="流程名" class="easyui-textbox" style="width:200px;">
        <input name="extra_finished" prompt="状态" class="easyui-combobox"  style="width:120px; "
               data-options="data: [{value:'true',text:'已结束'} ,{value:'false',text:'正运行'}], panelHeight:'auto',value:''">
        <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
           onclick="queryModel('dg','searchSpan')">搜索</a>
     </span>
    </div>
    <script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/process.js"></script>
    <#if (session.buttonCodes)?seq_contains('myApply:cancel') || (session.buttonCodes)?seq_contains('myApply:cancel')  >
    <script>
        function instanceCancel(instanceId) {
            popup.openConfirm(null,3, '流程作废', '流程作废 不可撤销，您确定要继续吗?', function () {
                $.post('${ctx!}/myApply/cancelApply?id='+instanceId, function (data) {
                    if(data.state==='ok'){
                        popup.msg(data.msg, function () {
                            $('#dg').datagrid('reload');
                        });
                    }else if(data.state==='error'){
                        popup.errMsg('系统异常',data.msg);
                    }else{
                        popup.msg(data.msg);
                    }
                }, "json").error(function(){ popup.errMsg();});
            })
        }
        function instanceDelete(instanceId) {
            popup.openConfirm(null,3, '流程删除(物理删除)', '流程删除不可撤销，您确定要继续吗?', function () {
                $.post('${ctx!}/myApply/deleteApply?id='+instanceId, function (data) {
                    if(data.state==='ok'){
                        popup.msg(data.msg, function () {
                            $('#dg').datagrid('reload');
                        });
                    }else if(data.state==='error'){
                        popup.errMsg('系统异常',data.msg);
                    }else{
                        popup.msg(data.msg);
                    }
                }, "json").error(function(){ popup.errMsg();});
            })
        }
        function opeFmt(v,row){
            var ret = '';
            <@hasBtnCode "myApply:cancel">
              if(isEmpty(row.endTime)){
                  ret += '<button class="pure-button button-xsmall"  onclick="instanceCancel(\''+row.instanceId+'\')">取消</button>';
              }
            </@hasBtnCode>
            <@hasBtnCode "myApply:delete">
               if(notEmpty(row.endTime)){
                   ret += '<button class="pure-button pure-button-danger button-xsmall"  onclick="instanceDelete(\''+row.instanceId+'\')">删除</button>';
               }
            </@hasBtnCode>
            return ret;
        }
    </script>
    </#if>
</@layout>

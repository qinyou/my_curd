<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/processDefine/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"x
       striped="false"  pagination="true" nowrap="false"
       data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
       },onLoadSuccess: function(){
              $(this).datagrid('autoMergeCells',['key'])
       }"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th field="id" hidden width="250" formatter="urlFmt">ID</th>
        <th field="key" align="center" width="150" formatter="urlFmt">定义key</th>
        <th field="name" width="200">名称</th>
        <th field="version" width="60">版本</th>
        <th field="description" width="500" formatter="paddingFmt">描述</th>
        <th field="state" width="100" formatter="stateFmt">状态</th>
        <th field="deploymentId" width="80" hidden>部署Id</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="deleteModel('dg','${ctx!}/processDefine/activateProcessDefine', '激活')"  class="easyui-linkbutton"
    iconCls="iconfont icon-add" plain="true">激活</a>

    <a onclick="deleteModel('dg','${ctx!}/processDefine/suspendProcessDefine','挂起')" href="#" class="easyui-linkbutton  "
    iconCls="iconfont icon-delete" plain="true">挂起</a>

    <a onclick="convertToModel()" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-convert" plain="true">转化为模型</a>

    <span id="searchSpan" class="searchInputArea">
         <input name="extra_name" prompt="名称" class="easyui-textbox" style="width:120px; ">
          <input name="extra_key" prompt="定义key" class="easyui-textbox" style="width:120px; ">
          <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/plugins/easyui/auto-merge.js"></script>
<script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
<script>
    function stateFmt(val){
       return val ? '启用':'禁用';
    }
    function urlFmt(val,row){
        return '<a class="underline" title="流程图详情" href="javascript:popup.openIframe(\'查看\',\'${ctx!}/static/plugins/activiti/diagram-viewer/index.html?processDefinitionId='
            +row.id+'\',\'990px\',\'600px\',true)"  >'+val+'</a>';
    }
    function convertToModel(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length === 1) {
            var url = '${ctx!}/processDefine/convertToModel?id='+rows[0].id;
            $.post(url, function (data) {
                if(data.state === 'ok'){
                    popup.msg(data.msg, function () {
                        $('#dg').datagrid('reload');
                    });
                }else if(data.state==='error'){
                    popup.errMsg('系统异常',data.msg);
                }else{
                    popup.msg(data.msg);
                }
            }, "json").error(function(){ popup.errMsg(); });
        } else {
            popup.msg('请选择一行数据进行转化');
        }
    }
</script>
</@layout>

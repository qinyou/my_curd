<#include "../common/common.ftl"/>
<@layout>
<style>
    #newModel {
        display: none;
    }
</style>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/processModel/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"x
       striped="false"  pagination="true"
       data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
       }"
       ctrlSelect="true" pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th field="id" width="150">ID</th>
        <th field="name" width="200">名称</th>
        <th field="description" width="400">描述信息</th>
        <th field="createTime" hidden width="180" hidden>创建时间</th>
        <th field="lastUpdateTime" hidden width="180">最后更新时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="localNewModel()" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新建</a>
    <a onclick="editProcessModel()" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/processModel/deleteAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>
    <a onclick="deleteSingleModel('dg','${ctx!}/processModel/modelDeploy','模型部署')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-deployment" plain="true">部署</a>
    <span id="searchSpan" class="searchInputArea">
          <input name="extra_id" prompt="ID" class="easyui-textbox" style="width:150px; ">
          <input name="extra_name" prompt="备注" class="easyui-textbox" style="width:150px; ">
          <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
    <div id="newModel">
        <div style="padding: 10px;">
            <form id="form" class="pure-form pure-form-stacked" autocomplete="off" type="post"
                  action="${ctx!}/processModel/newModel" target="_blank">
                <input name="name" type="text" class="pure-input-1" placeholder="名称">
                <textarea name="description" rows="4" class="pure-input-1" placeholder="说明"></textarea>
                <button type="button" onclick="handleCreateBtn()" class="pure-button pure-input-1 pure-button-primary">新建</button>
            </form>
            <script>
                function handleCreateBtn(){
                    if(!$('#form input[name=name]').val()){
                        popup.msg('名称参数 不可为空');
                        return;
                    }
                    window.open('${ctx!}/processModel/newModel?'+$("#form").serialize(),'_blank');
                    layer.closeAll('page');
                }
            </script>
        </div>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
<script>
    function editProcessModel(){
        let rows= $("#dg").datagrid("getSelections");
        if (rows.length === 1) {
            window.open("${ctx!}/static/plugins/activiti/modeler.html?modelId="+rows[0].id);
        } else {
            popup.msg('请选择一行数据编辑模型');
        }
    }
    function localNewModel(){
        popup.openDOM('新建模型',$('#newModel').html(),'400px','300px');
    }
</script>
</@layout>

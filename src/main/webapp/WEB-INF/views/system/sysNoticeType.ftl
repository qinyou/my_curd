<#include "../common/common.ftl"/> <@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/sysNoticeType/query"
       data-options="onDblClickRow:editModel"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="true"
       nowrap="false"
       striped="false"
       pagination="true"
       ctrlSelect="true"
       pageSize="40" pageList="[20,40]">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th field="logo" align="center" width="60" formatter="logoFmt">图标</th>
        <th field="cate" width="100">分类</th>
        <th field="typeName" width="100">名称</th>
        <th field="typeCode" width="100">编码</th>
        <th field="untilExpiryDay" width="100">过期天数</th>
        <th field="untilDeadDay" width="100">删除天数</th>
        <#--<th field="template" width="400">消息模板(ftl)</th>-->
        <th field="remark" width="400">备注</th>
        <th field="creater" width="100" formatter="usernameFmt">创建人</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/sysNoticeType/newModel',  '740px', '550px')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/sysNoticeType/newModel',  '740px', '550px')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/sysNoticeType/deleteAction')" href="#" class="easyui-linkbutton  " iconCls="iconfont icon-delete" plain="true">删除</a>
    <a onclick="newTypeRole()" href="#" class="easyui-linkbutton  " iconCls="iconfont icon-config" plain="true">配置角色</a>
    <span  id="searchSpan" class="searchInputArea" >
         <input name="search_LIKE_cate"   prompt="分类" class="easyui-textbox" style="width:120px; ">
         <input name="search_LIKE_typeName"   prompt="名称" class="easyui-textbox" style="width:120px; ">
         <input name="search_LIKE_typeCode"   prompt="编码" class="easyui-textbox" style="width:120px; ">
         <input name="search_LIKE_remark" prompt="备注" class="easyui-textbox" style="width:120px; ">
         <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function logoFmt(val) {
        if(isEmpty(val)){
            return '';
        }
        return '<a target="_blank" href="${ctx!}/'+ val + '"><img height="25px" src="${ctx!}/' + val + '" alt="logo"/></a>'
    }
    /**
     * 用户配置角色
     */
    function newTypeRole(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length==1) {
            popup.openIframe('配置角色','${ctx!}/sysNoticeType/newTypeRole?id=' + rows[0].id, '800px', '500px');
        } else {
            popup.msg('请选择一行数据配置角色');
        }
    }
</script>
</@layout>

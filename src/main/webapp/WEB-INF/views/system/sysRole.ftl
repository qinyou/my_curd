<#--数据字典 datagrid  -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
     url="${ctx!}/sysRole/query"
     toolbar="#tb" rownumbers="true" border="false"
     fitColumns="false"
     fit="true" pagination="true"
     ctrlSelect="true"
     striped="false"
     pageSize="40" pageList="[20,40]">
  <thead>
  <tr>
      <th data-options="field:'id',checkbox:true"></th>
      <th field="roleName" width="250">名称</th>
      <th field="roleCode" width="150">编码</th>
      <th field="roleDesc" width="300">说明</th>
      <th field="sortNum" width="50">排序号</th>
  </tr>
  </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/sysRole/newModel', '350px', '370px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/sysRole/newModel', '350px', '370px')" href="#"
       class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/sysRole/deleteAction')" href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-delete" plain="true">删除</a>
    <a onclick="openUsers()" href="#" class="easyui-linkbutton " iconCls="iconfont icon-look" plain="true">查看用户</a>
    <a onclick="openResources()" href="#" class="easyui-linkbutton  " iconCls="iconfont icon-config" plain="true">配置资源</a>
    <span id="searchSpan" class="searchInputArea">
            <input name="search_LIKE_roleName" prompt="名称" class="easyui-textbox" style="width:120px; ">
            <input name="search_LIKE_roleCode" prompt="编码" class="easyui-textbox" style="width:120px; ">
            <a href="#" class="easyui-linkbutton searchBtn"
               data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    /**
     *  角色相关用户
     */
    function openUsers(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length===1) {
            popup.openIframe('相关用户','${ctx!}/sysRole/users?id=' + rows[0].id, '800px', '400px');
        } else {
            popup.msg('请选择一行数据查看用户');
        }
    }

    /**
     * 角色配置菜单
     */
    function openResources(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length===1) {
            popup.openIframe('配置资源', '${ctx!}/sysRole/resources?id=' +rows[0].id,  '600px', '700px')
        } else {
            popup.msg('请选择一行数据进行操作');
        }
    }
</script>
</@layout>

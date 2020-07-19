<#--  zhangchuang 2019-03-01 08:42:39 -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"  url="${ctx!}/sysMenu/queryButtons?menuId=${menuId}"
       toolbar="#tb" rownumbers="false" border="false"
       fit="true"    fitColumns="true"  ctrlSelect="true"
       striped="false"  >
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th field="buttonTxt" width="80">名称</th>
        <th field="buttonCode" width="150">编码</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/sysMenu/newButtonModel?menuId=${menuId}', '320px', '200px')"  class="easyui-linkbutton" iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/sysMenu/newButtonModel', '320px', '200px')" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/sysMenu/deleteButtonAction?menuId=${menuId}')" class="easyui-linkbutton" iconCls="iconfont icon-delete" plain="true">删除</a>
    <a onclick="openRoles()" href="javascript:void(0)" class="easyui-linkbutton " iconCls="iconfont icon-look" plain="true"> 查看角色</a>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function openRoles(){
        var row= $("#dg").datagrid("getSelected");
        if (row!=null) {
            popup.openIframe('相关角色','${ctx!}/sysMenu/buttonRoles?id=' + row.id, '600px', '500px');
        } else {
            popup.msg('请选择一行数据查看相关角色');
        }
    }
</script>
</@layout>

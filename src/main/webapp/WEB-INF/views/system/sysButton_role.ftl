<#--角色相关用户-->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/sysMenu/queryButtonRole?search_EQ_a.sysButtonId=${buttonId!}"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="true"
       striped="true"   pageSize="40" pageList="[20,40]"
       pagination="true"
       ctrlSelect="true" >
    <thead>
    <tr>
        <th field="roleName"  width="100">角色名</th>
        <th field="roleCode" width="100" >角色编码</th>
        <th field="creater" width="100" formatter="usernameFmt">操作人</th>
        <th field="createTime" width="200">操作时间</th>
        <th field="sysRoleId" width="50" formatter="deleteFmt">操作</th>
    </tr>
    </thead>
</table>
<div id="tb" style="text-align: center; padding: 5px">
    <div id="searchSpan" class="searchInputAreaDiv" style="text-align: center" >
        <input name="search_LIKE_b.roleName" prompt="角色名" class="easyui-textbox" style="width:120px">
        <input name="search_LIKE_b.roleCode" prompt="角色编码" class="easyui-textbox" style="width:120px">
        <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
           onclick="queryModel('dg','searchSpan')">搜索</a>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function deleteFmt(val,row){
        return '<a href="javascript:deleteMenuRole(\''+row.sysButtonId+'\',\''+row.sysRoleId+'\')"> 删除 </a>'
    }

    /* 删除 单行 */
    function deleteMenuRole(buttonId,roleId) {
        $.get('${ctx!}/sysMenu/deleteButtonRole?buttonIdId='+buttonId+"&roleId="+roleId , function (data) {
            if(data.state=='ok'){
                popup.msg(data.msg, function () {
                    $('#dg').datagrid('reload');
                });
            }else if(data.state=='error'){
                popup.errMsg('系统异常',data.msg);
            }else{
                popup.msg(data.msg);
            }
        }, "json").error(function(){ popup.errMsg(); });
    }
</script>
</@layout>

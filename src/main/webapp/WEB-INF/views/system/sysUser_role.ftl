<#--用户配角色-->
<#include "../common/common.ftl"/>
<@layout>
      <table id="dg" class="easyui-datagrid"
             url="${ctx!}/sysUser/queryUserRole?search_EQ_a.sysUserId=${(userId)!}"
             toolbar="#tb" rownumbers="true" border="false"
             fitColumns="true"
             fit="true" pagination="true"
             ctrlSelect="true">
          <thead>
          <tr>
              <th data-options="field:'id',checkbox:true"></th>
              <th field="roleName" width="100">名称</th>
              <th field="roleCode" width="120">编码</th>
              <th field="roleDesc" width="300">说明</th>
              <th field="creater" width="100" formatter="usernameFmt">添加人</th>
          </tr>
          </thead>
      </table>
    <div id="tb">
        <a onclick="openUtilsRole(false,'添加角色')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-add" plain="true">添加</a>
        <a onclick="deleteRolesAction()" href="#" class="easyui-linkbutton" iconCls="iconfont icon-delete" plain="true">删除</a>
        <span id="searchSpan" class="searchInputArea">
             <input name="search_LIKE_b.roleName" prompt="名称" class="easyui-textbox" style="width:120px; ">
             <input name="search_LIKE_b.roleCode" prompt="编码" class="easyui-textbox" style="width:120px; ">
             <input name="search_LIKE_a.creater" prompt="添加人" class="easyui-textbox" style="width:120px; ">
             <a href="#" class="easyui-linkbutton searchBtn"
                data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索
             </a>
        </span>
    </div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    /**
     * 保存选中的角色，被其它窗口调用
     * @param roles
     */
    function addRolesAction(roles){
        var ids = [];
        roles.forEach(function(aryItem){
            ids.push(aryItem.id);
        });
        $.post('${ctx!}/sysUser/addUserRoleAction?roleIds=' + ids.join(',')+"&userId=${(userId)!}", function (data) {
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
        }, "json").error(function(){ popup.errMsg(); });
    }

    /**
     * 删除用户角色
     */
    function deleteRolesAction(){
        var rows = $("#dg").datagrid("getSelections");
        if (rows.length!=0) {
            popup.openConfirm(null,3, '删除', '您确定要删除选中的'+rows.length+'条记录吗?', function () {
                var roleIds = [];
                rows.forEach(function(row){
                    roleIds.push(row.sysRoleId);
                });
                $.post('${ctx!}/sysUser/deleteUserRoleAction?userId=${(userId)!}&roleIds=' + roleIds.join(','), function (data) {
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
            });
        } else {
            popup.msg('请至少选择一行进行删除');
        }
    }
</script>
</@layout>

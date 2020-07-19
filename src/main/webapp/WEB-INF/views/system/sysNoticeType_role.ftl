<#include "../common/common.ftl"/> <@layout>
<script type="text/javascript">
    /**
     * 保存选中的角色，被其它窗口调用
     * @param roles
     */
    function addRolesAction(roles){
        var ids = [];
        roles.forEach(function(aryItem){
            ids.push(aryItem.id);
        });
        $.post('${ctx!}/sysNoticeType/addTypeRoleAction?roleIds=' + ids.join(',')+"&sysNoticeTypeId=${typeId}", function (data) {
            if(data.state=='ok'){
                popup.msg(data.msg, function () {
                    $('#dg').datagrid('reload');
                    popup.closeByIndex(winIndex);
                });
            }else if(data.state=='error'){
                popup.errMsg('系统异常',data.msg);
            }else{
                popup.msg(data.msg);
            }
        }, "json").error(function(){ popup.errMsg(); });
    }

    function deleteTypeRoles() {
        var rows = $("#dg").datagrid("getSelections");
        if(rows.length==0){
            popup.msg('请至少选择一行进行删除');
            return;
        }
        popup.openConfirm(null,3, '删除', '您确定要删除选中的'+rows.length+'条记录吗?', function () {
            var idPairs = [];
            rows.forEach(function(row){
                idPairs.push(row.sysNoticeTypeId+","+row.sysRoleId);
            });
            $.post('${ctx!}/sysNoticeType/deleteTypeRoleAction?idPairs='+ idPairs.join(';'), function (data) {
                if(data.state==='ok'){
                    popup.msg(data.msg, function () {
                        $('#dg').datagrid('reload');
                    });
                }else if(data.state==='error'){
                    popup.errMsg('系统异常',data.msg);
                }else{
                    popup.msg(data.msg);
                }
            }, "json").error(function(){ popup.errMsg(); });
        });
    }
</script>
<div id="nestLayout" class="easyui-layout" fit="true"   >
    <div data-options="region:'center' " split="true"   collapsed="false"  headerCls="borderTopNone">
        <table id="dg" class="easyui-datagrid"
               url="${ctx!}/sysNoticeType/queryTypeRole?search_EQ_a.sysNoticeTypeId=${typeId}"
               toolbar="#tb" rownumbers="true" border="false"
               fit="true"    fitColumns="true"
               striped="false"  ctrlSelect="true"
               pagination="true"  pageSize="40" pageList="[20,40]">
            <thead>
            <tr>
                <th data-options="field:'sysNoticeTypeId',checkbox:true"></th>
                <th field="roleName" width="100">角色名</th>
                <th field="roleCode" width="150">编码</th>
                <th field="roleDesc" width="200">说明</th>
            </tr>
            </thead>
        </table>
        <div id="tb">
            <a onclick="openUtilsRole(false,'关联角色')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-add" plain="true">添加</a>
            <a onclick="deleteTypeRoles()" href="#" class="easyui-linkbutton  " iconCls="iconfont icon-delete" plain="true">删除</a>
            <span id="searchSpan" class="searchInputArea">
                <input name="search_LIKE_b.roleName" prompt="角色名" class="easyui-textbox" style="width:120px; ">
                <input name="search_LIKE_b.roleCode" prompt="编码" class="easyui-textbox" style="width:120px; ">
                <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg','searchSpan')">搜索</a>
            </span>
        </div>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

<#--数据字典 datagrid  -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
    url="${ctx!}/sysUser/query"
    toolbar="#tb" rownumbers="true" border="false"
    fitColumns="false"
    fit="true" pagination="true"
    ctrlSelect="true"
    striped="false"
    data-options="onHeaderContextMenu: function(e, field){
            e.preventDefault();
            $(this).datagrid('columnMenu').menu('show', {
                left:e.pageX,
                top:e.pageY
            });
      }"
    pageSize="40" pageList="[20,40]">
 <thead>
 <tr>
     <th data-options="field:'id',checkbox:true"></th>
     <th field="username" width="100" formatter="usernameFmt">用户名</th>
     <th field="realName" width="100">姓名</th>
     <th field="orgNames" width="300">部门</th>
     <th field="job" width="150">职位</th>
     <th field="phone" width="150" hidden="true">电话</th>
     <th field="email" width="180" hidden="true">邮箱</th>
     <th field="gender" width="50" hidden="true" formatter="genderFmt">性别</th>
     <th field="userState" width="100" align="center"  formatter="stateFmt">状态</th>
     <th field="lastLoginTime" width="200">上次登录时间</th>
 </tr>
 </thead>
</table>
<div id="tb">
    <a onclick="newModel('${ctx!}/sysUser/newModel', '650px', '450px')"   class="easyui-linkbutton"  iconCls="iconfont icon-add" plain="true">新增</a>
    <a onclick="editModel('dg','${ctx!}/sysUser/newModel', '650px', '450px')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
    <a onclick="deleteModel('dg','${ctx!}/sysUser/deleteAction')"   class="easyui-linkbutton  " iconCls="iconfont icon-delete" plain="true"><#--逻辑删除-->删除</a>
    <a onclick="resetPwd()"  class="easyui-linkbutton  " iconCls="iconfont icon-reset-pwd" plain="true">重置密码</a>
    <a onclick="newUserRole()"  class="easyui-linkbutton  " iconCls="iconfont icon-config" plain="true">配置角色</a>
    <span id="searchSpan" class="searchInputArea">
            <input name="search_LIKE_a.username" prompt="用户名" class="easyui-textbox" style="width:120px; ">
            <input name="search_LIKE_a.realName" prompt="姓名" class="easyui-textbox" style="width:120px; ">
            <input name="search_LIKE_a.job" prompt="职位" class="easyui-textbox" style="width:120px; ">
            <input name="search_EQ_a.userState" prompt="状态"  style="width:120px;"  class="easyui-combobox"
                   data-options="data:[{value:'0',text:'正常'} ,{value:'1',text:'禁用'}], panelHeight:'auto'" >
            <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"  onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script src="${ctx!}/static/js/input2combotree.js"></script>
<script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
    <script>
        function stateFmt(v) {
            return v === '0'?'正常':'禁用';
        }
        function genderFmt(val,row) {
            var gender = '';
            if(val==='M'){
                gender = '男';
            }else if(val==='F'){
                gender = '女';
            }
            return gender;
        }
    </script>
<script>
    function resetPwd(){
        var rows = $("#dg").datagrid("getSelections");
        if (rows.length!==0) {
            popup.openConfirm(null,3, '重置密码', '您确定要重置选中的 '+rows.length+'位 用户的密码吗?', function () {
                var ids = [];
                rows.forEach(function(row){
                    ids.push(row.id);
                });
                $.post('${ctx!}/sysUser/resetPwd?ids=' + ids.join(','), function (data) {
                    popup.msg(data.msg);
                }, "json").error(function(){ popup.errMsg(); });
            });
        } else {
            popup.msg('请至少选择一条数据');
        }
    }
    /**
     * 用户配置角色
     */
    function newUserRole(){
        var rows= $("#dg").datagrid("getSelections");
        if (rows.length===1) {
            popup.openIframe('已拥有角色','${ctx!}/sysUser/newUserRole?id=' + rows[0].id, '800px', '500px');
        } else {
            popup.msg('请选择一行数据配置角色');
        }
    }
</script>
</@layout>

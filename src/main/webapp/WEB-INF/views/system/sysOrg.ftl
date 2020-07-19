<#--组织机构 treegrid  -->
<#include "../common/common.ftl"/>
<@layout>
<div class="easyui-layout" fit="true" border="false">
    <div data-options="region:'west',split:true" border="false" style="width:30%;" collapsible="false" >
        <table id="tg" border="false"  ></table>
        <div id="tb">
            <a onclick="newModel('tg','${ctx!}/sysOrg/newModel', '690px', '400px')" href="#" class="easyui-linkbutton"  iconCls="iconfont icon-add" plain="true">新增</a>
            <a onclick="editModel('tg','${ctx!}/sysOrg/newModel', '690px', '400px')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
            <a onclick="deleteModel('tg','${ctx!}/sysOrg/deleteAction')" href="#" class="easyui-linkbutton  "  iconCls="iconfont icon-delete" plain="true">删除</a>
        </div>
    </div>

    <div data-options="region:'center'" border="false" class="bg" style="padding-left: 10px;">
        <table id="dg2" class="easyui-datagrid"
               url="${ctx!}/sysOrg/queryUser"
               toolbar="#tb2" rownumbers="true" border="false"
               fit="true" pagination="true"
               fitColumns="true"
               singleSelect="true"
               striped="false"
               pageSize="40" pageList="[20,40]">
            <thead>
            <tr>
                <th field="username" width="50" formatter="usernameFmt">用户名</th>
                <th field="realName" width="60">姓名</th>
                <th field="job" width="100">职位</th>
                <th field="roles" width="300">角色</th>
            </tr>
            </thead>
        </table>
        <div id="tb2">
            <a onclick="newUserRole()"  class="easyui-linkbutton  " iconCls="iconfont icon-config" plain="true">配置角色</a>
            <span id="searchSpan2" class="searchInputArea">
                <span style="display: inline-block;height: 30px;padding: 5px 10px;">
                  <input class="easyui-checkbox" id="cascadeSearch"  value="cascadeOrg" label="机构级联"  data-options="onChange:cascadeOrgChange" >
                </span>
                <input id="cascadeOrg" type="hidden" name="extra_cascadeOrg">
                <input id="orgId" type="hidden" name="extra_orgId">
                <input name="search_LIKE_a.username" prompt="用户名" class="easyui-textbox" style="width:120px; ">
                <input name="search_LIKE_a.realName" prompt="姓名" class="easyui-textbox" style="width:120px; ">
                <input name="search_LIKE_a.job" prompt="职位" class="easyui-textbox" style="width:120px; ">
                <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg2','searchSpan2')">搜索</a>
            </span>
        </div>
    </div>
</div>
<script src="${ctx!}/static/js/tg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script>
    function orgNameFmt(val,row) {
      return '<a href="javascript:orgInfo(\''+row.id+'\')" title="点击查看机构信息" >'+val+'</a>';
    }

    <#--级联机构查询 onChange 事件-->
    function cascadeOrgChange(checked){
        $("#cascadeOrg").val(checked);
        if(notEmpty($("#orgId").val())){
            $(".searchBtn","#searchSpan2").first().trigger('click');
        }
    }
    /**
     * 用户配置角色
     */
    function newUserRole(){
        var rows= $("#dg2").datagrid("getSelections");
        if (rows.length === 1) {
            popup.openIframe('已拥有角色','${ctx!}/sysUser/newUserRole?id=' + rows[0].id, '800px', '500px');
        } else {
            popup.msg('请选择一行数据配置角色');
        }
    }

    ;(function () {
        var easyTree = new EasyTree();
        $("#tg").treegrid({
            url: '${ctx!}/sysOrg/query',
            method: 'POST',
            idField: 'id',
            treeField: 'orgName',
            fit: true,
            lines:true,
            animate:true,
            fitColumns:true,
            rownumbers: true,
            toolbar: '#tb',
            loadFilter: function (data) {
                data = easyTree.treeDataBuild(data, 'id', 'pid', 'id,pid,orgName,sortNum,orgCode,state,iconCls');
                return data;
            },
            columns: [[
                {field: 'orgName', title: '机构名', width: 300,formatter:orgNameFmt},
                {field: 'orgCode', title: '代码', width: 120},
                {field: 'sortNum', title: '排序', width: 80}
            ]],
            onSelect: function (row) {
                $('#orgId').val(row.id);
                $("#cascadeOrg").val($('#cascadeSearch').checkbox('options').checked);
                $(".searchBtn","#searchSpan2").first().trigger('click');
            }
        });

    })();
</script>
</@layout>

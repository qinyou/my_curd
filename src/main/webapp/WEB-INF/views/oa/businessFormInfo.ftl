<#--  chuang 2019-08-22 20:49:00 -->
<#include "../common/common.ftl"/>
<@layout>
<div class="easyui-layout" fit="true" border="false">
    <div data-options="region:'west',split:true" border="false" style="width:300px;" collapsible="false" >
        <table id="tg" border="false"  ></table>
        <div id="tb" style="text-align: right">
            <span style="display: inline-block;height: 30px;padding: 5px 10px;">
                  <input class="easyui-checkbox" id="cascadeSearch" checked="true" value="cascadeOrg" label="级联查询"  data-options="onChange:cascadeOrgChange" >
            </span>
        </div>
    </div>

    <div data-options="region:'center'" border="false" class="bg" style="padding-left: 10px;">
        <table id="dg2" class="easyui-datagrid"
               url="${ctx!}/businessFormInfo/query"
               toolbar="#tb2" rownumbers="true" border="false"
               fit="true"    fitColumns="false"
               striped="false"  pagination="true"
               ctrlSelect="true" pageSize="40" pageList="[20,40]">
            <thead>
            <tr>
                <th data-options="field:'id',checkbox:true"></th>
                <#--<th field="categoryId" width="150">分类id</th>-->
                <th field="icon" align="center" width="50" formatter="dgCellIconFmt">图标</th>
                <th field="name" width="150">名字</th>
                <th field="info" width="250">描述信息</th>
                <th field="formName" width="150">业务表</th>
                <th field="processKey" width="150">流程Key</th>
            </tr>
            </thead>
        </table>
        <div id="tb2">
            <a onclick="newDictModel()" href="#" class="easyui-linkbutton"
               iconCls="iconfont icon-add" plain="true">新增</a>
            <a onclick="editModel('dg2','${ctx!}/businessFormInfo/newModel', '500px', '400px')" href="#"
               class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
            <a onclick="deleteModel('dg2','${ctx!}/businessFormInfo/deleteAction')" href="#" class="easyui-linkbutton  "
               iconCls="iconfont icon-delete" plain="true">删除</a>

            <span id="searchSpan2" class="searchInputArea"   >
                <#-- 前台传递 手动处理的 参数-->
                <input id="cascadeOrg" type="hidden" name="extra_cascadeOrg">
                <input id="orgId" type="hidden" name="extra_orgId">
                <#-- 拦截器 拼装sql-->
                <input name="search_LIKE_name" prompt="名称" class="easyui-textbox" style="width:180px; ">
                <input name="search_LIKE_info" prompt="描述" class="easyui-textbox" style="width:180px; ">
                 <input name="search_EQ_processKey" prompt="流程Key" class="easyui-textbox" style="width:180px; ">
                <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg2','searchSpan2')">搜索</a>
            </span>


        </div>
    </div>
</div>

<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script>
    /*新增字典弹窗*/
    function newDictModel(){
        var rows = $("#tg").datagrid("getSelections");
        if(rows.length===0 || rows.length>1){
            popup.msg('请选中一个机构');
            return;
        }
        popup.openIframe('新建','${ctx!}/businessFormInfo/newModel?categoryId='+rows[0].id, '500px', '400px')
    }
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
    ;(function () {
        var easyTree = new EasyTree();
        $("#tg").treegrid({
            url: '${ctx!}/utils/orgTreeData',
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
                data = easyTree.treeDataBuild(data, 'id', 'pid', 'id,pid,orgName,state,iconCls');
                return data;
            },
            columns: [[
                {field: 'orgName', title: '机构名', width: 300,formatter:orgNameFmt}
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

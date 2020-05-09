<#--数据字典 datagrid  -->
<#include "../common/common.ftl"/>
<@layout>
    <div class="easyui-layout" fit="true" border="false"  >
        <div data-options="region:'west',split:true" border="false"   style="width:40%;" collapsible="false"  >
            <table id="dg" class="easyui-datagrid"
                   url="${ctx!}/sysDict/queryGroup?search_IS_delFlag=null"
                   toolbar="#tb" rownumbers="true" border="false"
                   data-options="onSelect:groupSelect"
                   fitColumns="true"
                   fit="true" pagination="true"
                   ctrlSelect="true"
                   striped="false"
                   pageSize="40" pageList="[20,40]">
                <thead>
                <tr>
                    <th data-options="field:'id',checkbox:true"></th>
                    <th field="groupName" width="150">名称</th>
                    <th field="groupCode" width="200">编码</th>
                    <#--<th field="createTime" width="200">创建时间</th>-->
                </tr>
                </thead>
            </table>
            <div id="tb">
                <a onclick="newModel('${ctx!}/sysDict/newGroupModel', '330px', '200px')" href="#" class="easyui-linkbutton"
                   iconCls="iconfont icon-add" plain="true">新增分组</a>
                <a onclick="editModel('dg','${ctx!}/sysDict/newGroupModel', '330px', '200px')" href="#"
                   class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑分组</a>
                <a onclick="deleteModel('dg','${ctx!}/sysDict/deleteGroupAction')" href="#" class="easyui-linkbutton  "
                   iconCls="iconfont icon-delete" plain="true">删除分组</a>
                <span id="searchSpan" class="searchInputArea">
                    <input name="search_LIKE_groupName" prompt="名称" class="easyui-textbox" style="width:120px; ">
                    <input name="search_LIKE_groupCode" prompt="编码" class="easyui-textbox" style="width:120px; ">
                    <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                       onclick="queryModel('dg','searchSpan')">搜索</a>
                </span>
            </div>
        </div>

        <div data-options="region:'center'" border="false" class="bg"  style="padding-left: 10px;">
            <table id="dg2" class="easyui-datagrid"
                   url="${ctx!}/sysDict/queryDict?search_IS_delFlag=null"
                   toolbar="#tb2" rownumbers="true" border="false"
                   fit="true" pagination="true"
                   fitColumns="true"
                   ctrlSelect="true"
                   striped="false"
                   pageSize="40" pageList="[20,40]">
                <thead>
                <tr>
                    <th data-options="field:'id',checkbox:true"></th>
                    <th field="dictLabel" width="150">名称</th>
                    <th field="dictValue" width="200">编码</th>
                    <th field="sortNum" width="100">排序号</th>
                    <th field="state" width="100" >状态</th>
                    <#--<th field="createTime" width="200">创建时间</th>-->
                </tr>
                </thead>
            </table>
            <div id="tb2">
                <a onclick="newDictModel()" href="#" class="easyui-linkbutton" iconCls="iconfont icon-add" plain="true">新增字典</a>
                <a onclick="editModel('dg2','${ctx!}/sysDict/newDictModel', '350px', '350px')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑字典</a>
                <a onclick="deleteModel('dg2','${ctx!}/sysDict/deleteDictAction')" href="#" class="easyui-linkbutton  " iconCls="iconfont icon-delete" plain="true">删除字典</a>
                <span id="searchSpan2" class="searchInputArea">
                    <input id="groupCodeHid" name="search_EQ_groupCode" type="hidden"  >
                    <input name="search_LIKE_dictLabel" prompt="名称" class="easyui-textbox" style="width:120px; ">
                    <input name="search_LIKE_dictValue" prompt="编码" class="easyui-textbox" style="width:120px; ">
                    <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                       onclick="queryModel('dg2','searchSpan2')">搜索</a>
                </span>
            </div>
        </div>
    </div>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script>
        /*新增字典弹窗*/
        function newDictModel(){
            var rows = $("#dg").datagrid("getSelections");
            if(rows.length==0 || rows.length>1){
               popup.msg('请选中一条字典分组');
               return;
            }
            popup.openIframe('新建','${ctx!}/sysDict/newDictModel?groupCode='+rows[0].groupCode, '350px', '350px')
        }

        /*左侧分组选中*/
        function groupSelect(index,row){
            $('#groupCodeHid').val(row.groupCode);
            $('#searchSpan2 a').first().trigger('click');
        }
    </script>
</@layout>

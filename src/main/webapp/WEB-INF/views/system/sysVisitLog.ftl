<#--数据字典 datagrid  -->
<#include "../common/common.ftl"/>
<#include "../common/btnControl.ftl"/>
<@layout>
<style>
    /*datagrid 行号大时调整*/
    .datagrid-header-rownumber{
        width:50px !important;
    }
    .datagrid-cell-rownumber{
        width:50px !important;
    }
    /*datagrid 最小高度*/
    .datagrid-toolbar{
        min-height: 33px !important;
    }
</style>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/sysVisitLog/query" rownumbers="true" border="false" toolbar="#tb"
           data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
          }"
          fitColumns="false"
          fit="true" pagination="true"
          ctrlSelect="true"
          striped="false"
          pageSize="40" pageList="[10,20,40,80]">
       <thead>
       <tr>
           <th data-options="field:'id',checkbox:true"></th>
           <th field="url" width="300" formatter="urlFmt">请求地址</th>
           <th field="requestType" width="100">请求类型</th>
           <th field="sysUser" width="150"  formatter="usernameFmt" >用户</th>
           <th field="sysUserIp" width="200">IP地址</th>
           <th field="createTime" width="200">创建时间</th>
       </tr>
       </thead>
    </table>
    <div id="tb">
        <#-- 具有按钮编码权限才可显示 -->
        <@hasBtnCode "sysVisitLog:delete">
            <a onclick="deleteModel('dg','${ctx!}/sysVisitLog/deleteAction')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-delete" plain="true">删除</a>
        </@hasBtnCode>
        <@hasBtnCode "sysVisitLog:export">
            <a onclick="exportExcel('${ctx!}/sysVisitLog/exportExcel','searchSpan')" href="#" class="easyui-linkbutton" iconCls="iconfont icon-export" plain="true">导出</a>
        </@hasBtnCode>

        <span id="searchSpan" class="searchInputArea">
             <input name="search_LIKE_url" prompt="请求地址" class="easyui-textbox" style="width:150px; ">
            <input name="search_LIKE_sysUser" prompt="用户名" class="easyui-textbox" style="width:120px; ">
            <input name="search_LIKE_sysUserIp" prompt="IP地址" class="easyui-textbox" style="width:120px; ">
            <input name="search_LIKE_requestType" prompt="请求类型" class="easyui-combobox"  style="width:120px; "
                   data-options="data: [ {value:'GET',text:'GET'} ,{value:'POST',text:'POST'},{value:'PUT',text:'PUT'},{value:'DELETE',text:'DELETE'}],
                             editable: false, panelHeight:'auto'" >
            <input name="search_GTE_createTime"  prompt="创建时间起" class="easyui-datetimebox" >
            <input name="search_LTE_createTime" prompt="创建时间止" class="easyui-datetimebox" >

            <a href="#" class="easyui-linkbutton searchBtn"
               data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </span>
    </div>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/plugins/easyui1.8.5/datagrid-extend.js"></script>
    <script>
        function urlFmt(val,row){
            return '<a title="点击查看请求信息" href="javascript:viewModelsByLink(\'查看\',\'${ctx!}/sysVisitLog/view?id='
                    +row.id+'\',\'700px\',\'400px\')"  >'+val+'</a>';
        }
    </script>
</@layout>

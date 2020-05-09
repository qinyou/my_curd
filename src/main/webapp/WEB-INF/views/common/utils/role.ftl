<!--选择角色页面-->
<#include "../common.ftl"/>
<@layout>
<script type="text/javascript">
    function saveSelected(){
        var ary = $("#dg").datagrid("getSelections");
        if(ary.length==0){
            popup.msg('请至少选择一个角色');
            return;
        }
        if(top.location==self.location){
           return;
        }
        /*通过 iframe id  获得 窗口 并调用其方法*/
        top.frames[sessionStorage.getItem("iframeId")].addRolesAction(ary);
    }
</script>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/utils/queryRole"
       fitColumns="true"
       singleSelect="${singleSelect}"
       toolbar="#tb"
       rownumbers="true"
       border="false"
       fit="true" pagination="true">
    <thead>
    <tr>
        <#if singleSelect=="false"><th data-options="field:'id',checkbox:true"></th></#if>
        <th field="roleName" width="80">名称</th>
        <th field="roleCode" width="100">编码</th>
        <#--<th field="roleDesc" width="200">角色描述</th>-->
    </tr>
    </thead>
</table>
<div id="tb" >
    <a onclick="saveSelected()" href="#" class="easyui-linkbutton" iconCls="iconfont icon-save" plain="true">${yesBtnTxt!}</a>
    <span id="searchSpan" class="searchInputArea" >
          <input name="search_LIKE_roleName" prompt="名称" class="easyui-textbox" style="width:120px; ">
          <input name="search_LIKE_roleCode" prompt="编码" class="easyui-textbox" style="width:120px; ">
          <a href="#" class="easyui-linkbutton searchBtn"
            data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索
          </a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

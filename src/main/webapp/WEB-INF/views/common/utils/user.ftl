<!--选择用户弹窗-->
<#include "../common.ftl"/>
<@layout>
<script type="text/javascript">
    function saveSelected(){
        var ary = $("#dg").datagrid("getSelections");
        if(ary.length==0){
            popup.msg('请至少选择一个用户');
            return;
        }
        if(top.location==self.location){
           return;
        }
        var iframeId =sessionStorage.getItem("iframeId");
        top.frames[iframeId].addUsersAction(ary,'${yesBtnTxt!}');
    }
</script>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/utils/queryUser"
       fitColumns="true"
       singleSelect="${singleSelect}"
       toolbar="#tb"
       rownumbers="true"
       border="false"
       fit="true" pagination="true">
    <thead>
    <tr>
        <#if singleSelect=="false"><th data-options="field:'ID',checkbox:true"></th></#if>
        <th field="username" width="120" formatter="usernameFmt">用户名</th>
        <th field="realName" width="100">姓名</th>
        <th field="job" width="150">职位</th>
        <th field="orgNames" width="200">部门</th>
        <th field="userStateText" width="100">账号状态</th>
    </tr>
    </thead>
</table>
<div id="tb" >
    <a onclick="saveSelected()" href="#" class="easyui-linkbutton" iconCls="iconfont icon-save" plain="true">${yesBtnTxt!}</a>
    <span id="searchSpan" class="searchInputArea" >
        <input name="search_LIKE_a.username" prompt="用户名" class="easyui-textbox" style="width:120px; ">
        <input name="search_LIKE_a.realName" prompt="姓名" class="easyui-textbox" style="width:120px; ">
        <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

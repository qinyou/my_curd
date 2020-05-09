<!--通过角色查询用户列表-->
<#include "../common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/utils/queryUserByRole?search_EQ_h.roleCode=${roleCode!}"
       fitColumns="true" singleSelect="true"
       toolbar="#tb" rownumbers="true"
       border="false" fit="true" pagination="true">
    <thead>
    <tr>
        <th field="username" width="150">用户名</th>
        <th field="realName" width="100">姓名</th>
        <th field="orgName" width="200">部门</th>
        <th field="job" width="150">职位</th>
        <th field="userStateText" width="100">账号状态</th>
    </tr>
    </thead>
</table>
<div id="tb" >
    <span style="line-height: 32px;margin-left:10px;font-weight: bold"> 角色: ${roleName!} ( ${roleCode!} )</span>
    <span id="searchSpan" class="searchInputArea" >

        <input name="search_LIKE_a.username" prompt="用户名" class="easyui-textbox" style="width:120px; ">
        <input name="search_LIKE_a.realName" prompt="姓名" class="easyui-textbox" style="width:120px; ">
        <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

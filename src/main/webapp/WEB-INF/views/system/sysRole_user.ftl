<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/sysRole/queryUsers?search_EQ_a.sysRoleId=${roleId!}"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="true"
       striped="true"   pageSize="40" pageList="[20,40]"
       pagination="true"
       ctrlSelect="true" >
    <thead>
    <tr>
        <th  width="100" field="username" formatter="usernameFmt">登录名</th>
        <th  width="100" field="realName">姓名</th>
        <th  width="100" field="job">职位</th>
        <th  width="100" field="creater">操作人</th>
        <th  width="100" field="createTime">操作时间</th>
    </tr>
    </thead>
</table>
<div id="tb" style="text-align: center; padding: 5px">
    <div id="searchSpan" class="searchInputAreaDiv" style="text-align: center" >
      	 <input name="search_LIKE_b.username" prompt="登录名" class="easyui-textbox" style="width:120px">
         <input name="search_LIKE_b.realName" prompt="姓名" class="easyui-textbox" style="width:120px">
          <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
             onclick="queryModel('dg','searchSpan')">搜索</a>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

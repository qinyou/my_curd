<#--定时任务运行日志 datagrid  -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
     url="${ctx!}/sysTask/taskLogQuery?search_EQ_className=${className!}"
     toolbar="#tb" rownumbers="true" border="false"
     fitColumns="true"  fit="true" pagination="true"  ctrlSelect="true"
     striped="false"  pageSize="40" pageList="[20,40]">
  <thead>
  <tr>
      <th data-options="field:'id',checkbox:true"></th>
      <th field="result" width="100" formatter="resultFmt">运行结果</th>
  <#--<th field="className" width="250">类名</th>-->
      <th field="startTime" width="150">开始时间</th>
      <th field="endTime" width="150">结束时间</th>
      <#--<th field="error" width="200">错误信息</th>-->
  </tr>
  </thead>
</table>
<div id="tb">
    <a onclick="deleteModel('dg','${ctx!}/sysTask/deleteTaskLogAction')" href="#" class="easyui-linkbutton  "
           iconCls="iconfont icon-delete" plain="true">删除</a>
    <span id="searchSpan" class="searchInputArea">
            <input name="search_GTE_startTime" prompt="开始时间" class="easyui-datetimebox"  >
            <input name="search_LTE_endTime" prompt="结束时间" class="easyui-datetimebox"  >
            <input name="search_EQ_result" prompt="运行结果"   style="width:120px; "  class="easyui-combobox"
                  data-options="data: [ {value:'success',text:'success'} ,{value:'fail',text:'fail'}], editable: false, panelHeight:'auto'">
            <a href="#" class="easyui-linkbutton searchBtn"
               data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function resultFmt(val) {
        if(val=='success'){
            return '<font color="#40b370">'+val+'</font>'
        }
        return '<font color="red">'+val+'</font>'
    }
</script>
</@layout>
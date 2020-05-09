<#include "common/common.ftl"/><@layout>
<style>
    .noRead{
        color:#ff0000;
    }
    .noticeLogo{
        width: 25px;
        margin-top: 2px;
        border-radius: 100%;
    }
</style>
<script>
    function setRead(val,row){
        if(row.hasRead=='Y'){
            return;
        }
        $.post('${ctx!}/dashboard/noticeSetRead?detailId=' + row.detailId, function (data) {
            if(data.state==='ok'){
                popup.msg(data.msg, function () {
                    $('#dg').datagrid('reload');
                    parent.refreshCount();
                });
            }else if(data.state==='error'){
                popup.errMsg('系统异常',data.msg);
            }else{
                popup.msg(data.msg);
            }
        }, "json").error(function(){ popup.errMsg(); });
    }
    function setAllRead(){
        $.post('${ctx!}/dashboard/noticeSetAllRead', function (data) {
            if(data.state==='ok'){
                popup.msg(data.msg, function () {
                    $('#dg').datagrid('reload');
                    parent.refreshCount();
                });
            }else if(data.state==='error'){
                popup.errMsg('系统异常',data.msg);
            }else{
                popup.msg(data.msg);
            }
        }, "json").error(function(){ popup.errMsg(); });
    }
</script>
<div class="easyui-layout" fit="true" border="false">
    <div data-options="region:'center'" border="false" >
        <table id="dg" class="easyui-datagrid"
               url="${ctx!}/dashboard/noticeData"
               toolbar="#tb" rownumbers="true" border="false"
               data-options="onDblClickRow:setRead,rowStyler:function(index,row){
			      if (row.hasRead=='N'){
				      return 'background-color:#f5969659;';
			      }
		       }"
               fit="true" pagination="true"
               fitColumns="true" nowrap="false"
               singleSelect="true"  striped="false"
               pageSize="20" pageList="[10,20]">
            <thead>
            <tr>
                <th field="logo" align="center" width="60" formatter="logoFmt"></th>
                <th field="createTime" width="120"  >时间</th>
                <th field="title" width="80"  >标题</th>
                <th field="content" width="220" >通知内容</th>
            </tr>
            </thead>
        </table>
        <div id="tb">
            <a onclick="setAllRead()" href="#" class="easyui-linkbutton" iconCls="iconfont icon-all-read" plain="true">全部已读</a>
            <span id="searchSpan" class="searchInputArea">
                  <select name="search_EQ_c.hasRead" class="easyui-combobox"   style="width:100px; " data-options="panelHeight:'auto'">
                      <option value=""  selected>全部</option>
                      <option value="N">未读</option>
                      <option value="Y">已读</option>
                  </select>
                  <input name="search_EQ_a.title"  prompt="标题" class="easyui-textbox" style="width:120px; ">
                  <input name="search_GTE_a.createTime"  prompt="时间起" class="easyui-datetimebox"  >
                  <input name="search_LTE_a.createTime"  prompt="时间止" class="easyui-datetimebox" >
                  <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索</a>
            </span>
        </div>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function logoFmt(val,) {
        if(isEmpty(val)){
            return '';
        }
        return '<img  class="noticeLogo" src="${ctx!}/' + val + '" alt="logo"/>'
    }

</script>
</@layout>

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
    .contentDetail{
        padding: 20px 15px 20px 0px;
        font-size: 14px;
    }
    .date{
        padding: 0px 5px 20px 0px;
        font-size: 12px;
    }
</style>
<script>
    function setRead(val,row,index){
        if(row.hasRead === 'Y'){
            return;
        }
        $.post('${ctx!}/dashboard/noticeSetRead?detailId=' + row.detailId, function (data) {
            if(data.state==='ok'){
                /*消息设置为已读，更新行*/
                row.hasRead = 'Y';
                $('#dg').datagrid('updateRow',{
                    index: index,
                    row: {data:row}
                });

                parent.refreshCount();
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
        <table id="dg"  toolbar="#tb" rownumbers="true" border="false" showHeader="false"
               fit="true" pagination="true"
               fitColumns="true" nowrap="false"
               singleSelect="true"  striped="false"
               pageSize="20" pageList="[10,20]">
            <thead>
            <tr>
                <th field="logo" align="center" width="10" formatter="logoFmt"></th>
                <th field="title" width="80"  >标题</th>
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

                  <span class="hidden-xs">
                      <input name="search_EQ_a.title"  prompt="标题" class="hidden-xs easyui-textbox " style="width:120px; ">
                      <input name="search_GTE_a.createTime"  prompt="时间起" class="hidden-xs easyui-datetimebox"  >
                      <input name="search_LTE_a.createTime"  prompt="时间止" class="hidden-xs easyui-datetimebox" >
                  </span>

                  <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true" onclick="queryModel('dg','searchSpan')">搜索</a>
            </span>
        </div>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/plugins/easyui/datagrid-detailview.js"></script>
<script>
    function logoFmt(val,) {
        if(isEmpty(val)){
            return '';
        }
        return '<img  class="noticeLogo" src="${ctx!}/' + val + '" alt="logo"/>';
    }
</script>
<script>
    $(function(){
        $('#dg').datagrid({
            url:'${ctx!}/dashboard/noticeData',
            view: detailview,
            detailFormatter:function(index,row){
                return '<div class="contentDetail">'+row.content+'</div><div class="date secondary-text">时间: '+row.createTime+'</div>';
            },
            onExpandRow: function(index,row){
                $('#dg').datagrid('fixDetailRowHeight',index);
                setRead(null,row,index);
            },
            rowStyler:function(index,row){
                if (row.hasRead=='N'){
                    return 'background-color:#f5969659;';
                }
            }
        });
        var pager = $('#dg').datagrid('getPager');
        pager.pagination({
            showPageList: false,
            showRefresh: false,
            displayMsg: ''
        });
    });
</script>
</@layout>

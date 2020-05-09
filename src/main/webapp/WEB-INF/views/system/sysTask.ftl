<#--定时任务 datagrid  -->
<#include "../common/common.ftl"/>
<@layout>
<table id="dg" class="easyui-datagrid"
       url="${ctx!}/sysTask/query"
       toolbar="#tb" rownumbers="true" border="false"
       fitColumns="true"
       fit="true" pagination="false"
       singleSelect="true"   striped="false">
    <thead>
    <tr>
        <th field="name" width="150">名称</th>
        <th field="cron" width="150">cron表达式</th>
        <th field="className" width="350">任务类名</th>
        <th field="daemon" width="150">守护进程</th>
        <th field="enable" width="150">是否启用</th>
        <th field="runOnce" width="200" formatter="optFmt">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div id="searchSpan" class="searchInputAreaDiv"  >
            <input name="extra_name" prompt="名称" class="easyui-textbox" style="width:120px; ">
            <input name="extra_class" prompt="类名" class="easyui-textbox" style="width:120px; ">
            <input name="extra_daemon" prompt="是否守护" class="easyui-combobox"
                   data-options=" data: [{value:'true',text:'是'} ,{value:'false',text:'否'}], panelHeight:'auto'"
                   style="width:120px; ">
            <input name="extra_enable" prompt="是否启用" class="easyui-combobox"
                   data-options="data: [{value:'true',text:'是'} ,{value:'false',text:'否'}], panelHeight:'auto',value:''"
                   style="width:120px; ">
            <a href="#" class="easyui-linkbutton searchBtn"
               data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </div>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script>
    function runOnce(btn,classNameStr){
        var index = popup.loading(0);
       /* // 0代表加载的风格，支持0-2*/
        $(btn).text('正在运行').addClass('pure-button-disabled');
        /*alert("loadding");*/
        $.post('${ctx!}/sysTask/runOnce',{className:classNameStr},function (data) {
            popup.closeByIndex(index);
            if(data.state==='ok'){
                popup.msg(data.msg,function () {
                    $(btn).text('运行一次').removeClass('pure-button-disabled');
                });
            }else if(data.state==='error'){
                popup.errMsg('系统异常',data.msg);
                $(btn).text('运行一次').removeClass('pure-button-disabled');
            }else{
                popup.msg(data.msg,function () {
                    $(btn).text('运行一次').removeClass('pure-button-disabled');
                });
            }
        }, "json").error(function(){
            popup.closeByIndex(index);
            popup.errMsg();
            $(btn).text('运行一次').removeClass('pure-button-disabled');
        });
    }
    function viewLog(className) {
        popup.openIframe('定时任务日志', '${ctx!}/sysTask/taskLog?className='+className, '800px','500px');
    }

    function optFmt(val,row) {
        return '<button class="pure-button pure-button-primary button-xsmall" onclick="runOnce(this,\''+row.className+'\')">运行一次</button>'
                +'<button style="margin-left:10px" class="pure-button button-xsmall"  onclick="viewLog(\''+row.className+'\')">查看日志</button>';
    }
</script>
</@layout>
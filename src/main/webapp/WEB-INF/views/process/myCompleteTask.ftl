<#include "../common/common.ftl"/>
<@layout>
    <style>
        .del{
            display: inline-block;
            padding: 3px 5px;
            color:#fff;
            background-color:#33333363;
        }
    </style>
    <table id="dg" class="easyui-datagrid"
           url="${ctx!}/myCompleteTask/query"
           data-options="onHeaderContextMenu: function(e, field){
                e.preventDefault();
                $(this).datagrid('columnMenu').menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
           },onLoadSuccess: function(){
              $(this).datagrid('autoMergeCells',['instanceId'])
           }"
           toolbar="#tb" rownumbers="true" border="false"
           fit="true"    fitColumns="false" nowrap="false"
           striped="false"  pagination="true" singleSelect="true"
           pageSize="40" pageList="[30,40,50]">
        <thead>
        <tr>
            <th field="instanceId" width="400"  formatter="instanceIdFmt">流程信息</th>
            <th field="taskName" width="150">任务名</th>
            <th hidden field="startTime" width="200"><#--任务-->开始时间</th>
            <th hidden field="endTime" width="200"><#--任务-->结束时间</th>
            <th field="duration" width="200" align="center" formatter="durationFmt">任务耗时</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <div id="searchSpan" class="searchInputAreaDiv">
            <input name="extra_instanceId" prompt="流程ID" class="easyui-textbox" style="width:180px;">
            <span class="blankSep"></span>
            <input name="extra_taskName" prompt="任务名" class="easyui-textbox" style="width:180px;">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/easyui/datagrid-extend.js"></script>
    <script src="${ctx!}/static/plugins/easyui/auto-merge.js"></script>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
    <script src="${ctx!}/static/js/process.js"></script>
    <script>
        function instanceIdFmt(val,row) {
           var ret = '<div style="padding:5px">';
           ret +=  '流程id：'+row.instanceId+ '<br/>';
           ret +=  '流程名：<a class="underline" title="点击查看流程详情" href="javascript:openProcessInstanceDetail(\''+row.instanceId+'\')">'+row.instanceName+'</a><br/>';
           ret += '申请人：<a title="点击查看人员信息" class="underline" href="javascript:userInfo(\''+row.startUser+'\')" >'+row.startUser+'</a>';
           ret += '</div>';
           return ret;
        }

        function durationFmt(val){
            val = val/1000;
            var days = Math.floor(val / 86400);
            var hours = Math.floor((val % 86400) / 3600);
            var minutes = Math.floor(((val % 86400) % 3600) / 60);
            var seconds = Math.floor(((val % 86400) % 3600) % 60);
            var ret ;
            if(days !== 0){
                ret = days + "天" + hours + "小时" + minutes + "分";
            }else{
                if(hours !==0){
                    ret = hours + "小时" + minutes + "分";
                }else{
                    if(minutes !==0){
                        ret = minutes + "分";
                    }else{
                        if(seconds !==0){
                            ret = seconds + "秒";
                        }else{
                            ret = '';
                        }
                    }
                }
            }
            return ret;
        }
    </script>
</@layout>

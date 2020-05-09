<#-- zhangchuang  2019-07-23 15:50:30 -->
<#include "../common/common.ftl"/>
<@layout>
    <style>
        body{
            padding: 20px 10px;
        }
    </style>
    <link rel="stylesheet" href="${ctx!}/static/css/oa.css">
    <div class="instance-detail-title">
        ${processInstanceName!}
    </div>
    <div class="instance-detail-second-title">
        申请人:
        <a title="点击查看详细信息" href="javascript:userInfo('${initiator!}')">${initiator!}</a>
    </div>
    <div class="easyui-panel" title="申请内容" style="width:100%;margin-bottom: 20px;"
         data-options="href:'${ctx!}/oa/processInstanceFormDetail?businessForm=${businessForm!}&businessKey=${businessKey}',
         collapsible:true,closable:true">
    </div>

    <div class="easyui-panel" title="审批流转" style="width:100%;margin-bottom: 20px"
         data-options="href:'${ctx!}/oa/historicTaskInstances?id=${processInstanceId!}',
         collapsible:true,closable:true">
    </div>

    <div class="easyui-panel" title="流程图" style="width:100%;"
         data-options="collapsible:true,closable:true">
        <img style="max-width: 100%;" src="${ctx!}/oa/instanceDiagram?id=${processInstanceId!}" alt="流程图">
    </div>
    <#--<div class="easyui-tabs" fit="true" plain="true">-->
        <#--<div title="申请详情" style="padding:10px">-->
            <#--<div class="userCard">-->
                <#--${processInstanceName!}-->
            <#--</div>-->
            <#--<iframe   frameborder="0"  src="${ctx!}/oa/processInstanceFormDetail?businessForm=${businessForm!}&businessKey=${businessKey}" style="width:100%;height: auto"></iframe>-->
            <#--<img style="width: 100%;max-width: 1000px;" src="${ctx!}/oa/instanceDiagram?id=${processInstanceId!}" alt="流程图">-->
        <#--</div>-->
        <#--<div title="流程图" fit="true">-->
            <#--<img style="width: 100%;max-width: 1000px;" src="${ctx!}/oa/instanceDiagram?id=${processInstanceId!}" alt="流程图">-->
        <#--</div>-->
    <#--</div>-->
</@layout>

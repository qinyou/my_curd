<#-- 工具，流程详情页面 -->
<#include "../common/common.ftl"/>
<@layout>
    <link rel="stylesheet" href="${ctx!}/static/css/form-tpl.css">
    <link rel="stylesheet" href="${ctx!}/static/plugins/validform/css/style.css">
    <style>
        body{
            padding: 20px 10px;
        }
    </style>
    <link rel="stylesheet" href="${ctx!}/static/css/oa.css">
    <div class="instance-detail-title">
        ${instanceName!}
    </div>
    <div class="instance-detail-second-title">
        申请人:
        <a class="underline" title="点击查看详细信息" href="javascript:userInfo('${initiator!}')">${initiator!}</a>
        , 状态:
        <#if endTime??>
            <#if delReason??>
                 已取消
            <#else>
                运行结束
            </#if>
        <#else>
             运行中
        </#if>
    </div>
    <div id="app">
        ${applyFormTpl!}
    </div>
    <div class="easyui-panel" title="审批流转" style="width:100%;margin-bottom: 10px;"
         data-options="href:'${ctx!}/process/historicTaskInstances?id=${instanceId!}',collapsible:true">
    </div>
    <script src="${ctx!}/static/plugins/validform/js/jquery-1.9.1.min.js"></script>
    <script src="${ctx!}/static/plugins/validform/js/Validform_v5.3.2_min.js"></script>
    <script src="${ctx!}/static/plugins/vue/vue.min.js"></script>
    <script src="${ctx!}/static/js/form-tpl.js"></script>
    <script>
       $(function(){
           vm.editable = false;
           vm.form = JSON.parse('${applyFormData!"{}"}')
       });
    </script>
</@layout>

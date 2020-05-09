<#-- zhangchuang  2019-07-23 15:50:30 -->
<#include "../common/common.ftl"/>
<@layout>
    <link rel="stylesheet" href="${ctx!}/static/css/oa.css">
    <div class="easyui-tabs" fit="true" plain="true">
        <div title="流程办理" style="padding:10px">
            <div class="instance-detail-title">
                ${processInstanceName!}
            </div>
            <div class="instance-detail-second-title">
                申请人:
                <a title="点击查看详细信息" href="javascript:userInfo('${initiator!}')">${initiator!}</a>
            </div>

            <div class="easyui-panel" title="申请内容" style="width:100%;margin-bottom: 10px;"
                 data-options="href:'${ctx!}/oa/processInstanceFormDetail?businessForm=${businessForm!}&businessKey=${businessKey}',
                   collapsible:true,closable:true">
            </div>
            <#if taskDefinitionKey?? && taskDefinitionKey=='adjustForm'>
                <div style="margin-bottom: 20px;text-align: right;">
                    <button onclick="openAdjustForm('${ctx!}/${adjustFormUrl!}')" class=" button-small pure-button pure-button-primary ">调整申请</button>
                </div>
            </#if>

            <div class="easyui-panel" title="审批流转" style="width:100%;margin-bottom: 20px"
                 data-options="href:'${ctx!}/oa/historicTaskInstances?id=${processInstanceId!}',
                  collapsible:true,closable:true">
            </div>

            <div class="easyui-panel" title="${taskName!}" style="width:100%;margin-bottom: 20px;"
                 data-options="collapsible:true,closable:true">
                <form id="processForm" method="POST" action="${ctx!}/myTask/completeAction">
                    <input type="hidden" name="id" value="${taskId!}">
                    <input type="hidden" name="processInstanceId" value="${processInstanceId!}">
                    <table class=" pure-table pure-table-horizontal  labelInputTable fullWidthTable"
                           style="border-top: none;border-left: none;border-right: none;">
                        <#if taskDescription?? >
                            <tr>
                                <td>注意事项:</td>
                                <td style="color: #fc5832;font-weight:bold; font-size: 20px;">${taskDescription!}</td>
                            </tr>
                        </#if>
                        ${renderedTaskForm!}
                        <tr>
                            <td>备注（操作阐述）：</td>
                            <td>
                                <input id="comment" name="comment" class="easyui-textbox" multiline="true"
                                       required="true" style="width:90%; height:80px" value="正常通过"/>
                            </td>
                        </tr>

                    </table>
                </form>
                <div style="padding: 10px; text-align: right;background-color: #fafafa">
                    <button class=" button-small pure-button pure-button-primary"
                            onclick="completeAction('processForm','reload','dg')">办理
                    </button>
                </div>
            </div>

        </div>
        <div title="流程图" fit="true">
            <img style="max-width:100%;" src="${ctx!}/oa/instanceDiagram?id=${processInstanceId!}" alt="流程图">
        </div>
    </div>
    <script>
        function openAdjustForm(url){
            popup.openIframe('申请调整', url, '500px', '500px');
        }
        function completeAction(dgId) {
            $('#processForm').form('submit', {
                onSubmit: function (param) {
                    param.xmlHttpRequest = "XMLHttpRequest";
                    return $(this).form('validate');
                },
                success: function (data) {
                    if (typeof data === 'string') {
                        data = JSON.parse(data);
                    }
                    if (data.state === 'ok') {
                        popup.msg(data.msg, function () {
                            top.frames[sessionStorage.getItem("iframeId")].$("#dg").datagrid("reload");
                            popup.close(window.name);
                        });
                    } else if (data.state === 'error') {
                        popup.errMsg('系统异常', data.msg);
                    } else {
                        popup.msg(data.msg);
                    }
                }
            });
        }

    </script>
</@layout>

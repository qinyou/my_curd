<#include "../common/common.ftl"/>
<@layout>
    <link rel="stylesheet" href="${ctx!}/static/css/form-tpl.css">
    <link rel="stylesheet" href="${ctx!}/static/plugins/validform/css/style.css">
    <link rel="stylesheet" href="${ctx!}/static/css/oa.css">
    <div class="easyui-tabs" fit="true" plain="true">
        <div title="流程办理" style="padding:10px">
            <div class="instance-detail-title">
                ${instanceName!}
            </div>
            <div class="instance-detail-second-title">
                申请人:<a title="点击查看详细信息" href="javascript:userInfo('${initiator!}')">${initiator!}</a>
            </div>
            <div id="app">
                <#if adjustAble >
                    <form id="applyForm" autocomplete="off">
                    ${applyFormTpl!}
                    </form>
                    <div class="btnWrapper">
                        <button type="button" onclick="$('#applyForm').submit()" class="btn">提交</button>
                    </div>
                    <script>
                        var postonce = false;
                        function handleSubmit(){
                            var check = confirm("确认提交申请？");
                            var applyFormTitle = $('#applyFormTitle').val();
                            if (check) {
                                console.log(JSON.stringify(vm.form, null, 2));
                                $.ajax({
                                    url:"${ctx!}/myTask/adjustApply",
                                    type:"post",
                                    data: {
                                        taskId: '${taskId}',
                                        applyFormData: JSON.stringify(vm.form),
                                    },
                                    success: function (ret) {
                                        console.log(JSON.stringify(ret))
                                    }
                                })
                            }
                        }
                    </script>
                <#else>
                    ${applyFormTpl!}
                </#if>
            </div>
            <div class="easyui-panel" title="${taskName!}" style="width:100%;margin-bottom: 20px">
                <form id="taskForm" method="POST" action="${ctx!}/myTask/completeAction">
                    <input type="hidden" name="taskId" value="${taskId!}">
                    <input type="hidden" name="instanceId" value="${instanceId!}">
                    <table class="pure-table pure-table-horizontal  labelInputTable fullWidthTable" style="border-top: none;border-left: none;border-right: none;">
                        <#if taskDescription?? >
                            <tr>
                                <td>注意事项:</td>
                                <td style="color: #fc5832;font-weight:bold; font-size: 20px;">${taskDescription!}</td>
                            </tr>
                        </#if>
                        ${renderedTaskForm!}
                        <tr>
                            <td>意见建议(非必须)：</td>
                            <td>
                                <input name="comment" class="easyui-textbox" multiline="true" style="width:90%; height:80px" value="同意"/>
                            </td>
                        </tr>
                        <tr>
                            <td>附件(非必须)：</td>
                            <td>
                                <button onclick="handleUploadBtn()"  class="pure-button button-small" type="button">上传附件</button>
                                <div id="fileList">
                                </div>
                                <div style="display: none;"><input id="file" type="file" onchange="handleFileChange()"></div>
                            </td>
                        </tr>
                    </table>
                </form>
                <div style="padding: 10px; text-align: right;background-color: #fafafa">
                    <button class=" button-small pure-button pure-button-primary" onclick="completeAction('processForm','reload','dg')">办理</button>
                </div>
            </div>
        </div>

        <div title="流转信息" fit="true" href="${ctx!}/process/historicTaskInstances?id=${instanceId!}">
        </div>
        <#--<script src="${ctx!}/static/plugins/validform/js/jquery-1.9.1.min.js"></script>-->
        <script src="${ctx!}/static/plugins/validform/js/Validform_v5.3.2_min.js"></script>
        <script src="${ctx!}/static/plugins/vue/vue.min.js"></script>
        <script src="${ctx!}/static/js/form-tpl.js"></script>
        <#--日期插件-->
        <#if adjustAble >
            <#if applyFormTplPlugins?? && applyFormTplPlugins?contains('DatePicker')>
                <script src="${ctx!}/static/plugins/My97DatePicker/WdatePicker.js"></script>
                <script>
                    var needDatePicker = true;
                </script>
            </#if>
        </#if>
        <script>
            $(function(){
                vm.editable = <#if adjustAble>true<#else>false</#if >;
                vm.form = JSON.parse('${applyFormData!"{}"}');
                console.log('over');
                vm.taskKey = '${taskDefinitionKey!}';
            });
        </script>
    </div>
    <script>
        function openAdjustApply(url){
            popup.openIframe('申请调整', url, '500px', '500px');
        }
        function completeAction(dgId) {
            $('#taskForm').form('submit', {
                onSubmit: function (param) {param.xmlHttpRequest = "XMLHttpRequest";return $(this).form('validate');},
                success: function (data) {
                    if (typeof data === 'string') {data = JSON.parse(data);}
                    if (data.state === 'ok') {
                        var winName = getQueryVariable('winName');
                        popup.msg(data.msg, function () {top.frames[winName].$("#dg").datagrid("reload");  popup.close(window.name);});
                    }
                    else if (data.state === 'error') {popup.errMsg('系统异常', data.msg)}
                    else {popup.msg(data.msg)}
                }
            });
        }
    </script>
</@layout>

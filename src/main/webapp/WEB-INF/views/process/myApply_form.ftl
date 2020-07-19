<@compress single_line=true><!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="x-ua-compatible" content="ie=edge,chrome=1">
        <meta content=always name=referrer>
        <link rel="shortcut icon" href="${ctx!}/static/image/favicon.ico"/>
        <title>新建申请</title>
        <link rel="stylesheet" href="${ctx!}/static/css/form-tpl.css">
        <link rel="stylesheet" href="${ctx!}/static/plugins/validform/css/style.css">
    </head>
    <body>
    <div id="app">
        <div class="formWrapper">
            <form id="applyForm" autocomplete="off">
                <div style="text-align: center;padding: 10px">
                    申请标题：<input type="text" id="applyFormTitle" value="${applyFormTitle!}" style="padding:5px;width: 500px;font-size: 16px">
                </div>
                ${(applyFormTpl)!'表单模板为空'}
            </form>
            <div class="btnWrapper">
                <button v-if="editable" type="button" onclick="$('#applyForm').submit()" class="btn">提交</button>
            </div>
        </div>
    </div>
    <script src="${ctx!}/static/plugins/validform/js/jquery-1.9.1.min.js"></script>
    <script src="${ctx!}/static/plugins/validform/js/Validform_v5.3.2_min.js"></script>
    <script src="${ctx!}/static/plugins/vue/vue.min.js"></script>
    <script src="${ctx!}/static/js/utils.js"></script>
    <#--日期插件-->
    <#if applyFormTplPlugins?? && applyFormTplPlugins?contains('DatePicker')>
        <script src="${ctx!}/static/plugins/My97DatePicker/WdatePicker.js"></script>
        <script>
            var needDatePicker = true;
        </script>
    </#if>
    <script>
        var postonce = <#if test??>false<#else>true</#if>; <#--有测试参数，表单可以多次提交-->
        function handleSubmit(){
            var applyFormTitle = $('#applyFormTitle').val();
            top.popup.openConfirm(null,3, '提交确认', '确认提交申请?', function () {
                console.log(JSON.stringify(vm.form, null, 2));
                $.ajax({
                    url:"${ctx!}/myApply/newApplyAction",
                    type:"post",
                    dataType:'json',
                    data: {
                        processKey: '${processKey!}',
                        applyFormTplId: '${applyFormTplId!}',
                        applyFormTitle: isEmpty(applyFormTitle)?'${applyFormTitle!}':applyFormTitle,
                        applyFormData: JSON.stringify(vm.form),
                    },
                    success: function (data) {
                        console.log(JSON.stringify(data));
                        if(data.state ==='ok'){
                            top.popup.msg(data.msg, function () {
                                top.frames[sessionStorage.getItem("iframeId")].$('#dg').datagrid('reload');
                                top.popup.close(window.name);
                            });
                        }else if(data.state==='error'){
                            top.popup.errMsg('系统异常',data.msg);
                        }else{
                            top.popup.msg(data.msg);
                        }
                    },
                    error: function(){
                        top.popup.errMsg();
                    }
                })
            });
        }
    </script>
    <script src="${ctx!}/static/js/form-tpl.js"></script>
    </body>
</html>
</@compress>

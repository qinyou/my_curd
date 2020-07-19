<@compress single_line=true><!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="x-ua-compatible" content="ie=edge,chrome=1">
        <meta content=always name=referrer>
        <link rel="shortcut icon" href="${ctx!}/static/image/favicon.ico"/>
        <title>表单模板预览</title>
        <link rel="stylesheet" href="${ctx!}/static/css/form-tpl.css">
        <link rel="stylesheet" href="${ctx!}/static/plugins/validform/css/style.css">
        <#include "../common/custom.ftl"/>
        <script src="${ctx!}/static/plugins/validform/js/jquery-1.9.1.min.js"></script>
        <#include "../common/popup.ftl"/>
    </head>
    <body>
       <div id="app">
           <div class="formWrapper">
               <div style="padding: 20px 0; font-weight: 300;font-size: 14px;">${tplRemark!}</div>
               <form id="applyForm" autocomplete="off">
                   <#if dev??>
                       <#include "../common/formtpl-dev.ftl"/>
                   <#else>
                       ${(template)!}
                   </#if>
               </form>
               <div class="btnWrapper">
                   <button v-if="editable" type="button" onclick="$('#applyForm').submit()" class="btn">提交</button>
               </div>
               <div style="padding: 10px;border:1px solid #f3f3f3 ">
                   <div class="sub-title">表单数据</div>
                   <pre id="formData">
               </pre>
               </div>
           </div>
       </div>
       <script src="${ctx!}/static/plugins/validform/js/Validform_v5.3.2_min.js"></script>
       <script src="${ctx!}/static/plugins/vue/vue.min.js"></script>
       <#--日期插件-->
       <#if dev?? || (plugins?? && plugins?contains('DatePicker')) >
       <script src="${ctx!}/static/plugins/My97DatePicker/WdatePicker.js"></script>
       <script>
           var needDatePicker = true;
       </script>
       </#if>
       <script>
           var postonce = false;
           function handleSubmit(){
               var check = confirm("当前提交不会发起流程，仅预览标案提交数据，是否继续？");
               if (check) {
                   var formData = JSON.stringify(vm.form, null, 2);
                   console.log(formData);
                   $('#formData').text(formData);
               }
           }
       </script>
       <script src="${ctx!}/static/js/form-tpl.js"></script>
    </body>
    </html>
</@compress>

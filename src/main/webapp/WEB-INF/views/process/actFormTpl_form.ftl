<#-- chuang  2020-06-30 16:48:40 -->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" enctype="multipart/form-data"  action="<#if actFormTpl?? >${ctx!}/actFormTpl/updateAction<#else>${ctx!}/actFormTpl/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="${(actFormTpl.id)!}">
        <tbody>
        <tr>
            <td>
                表单key:
            </td>
            <td>
                <#if actFormTpl??>
                    ${(actFormTpl.formKey)!}
                    <#else>
                    <input name="formKey"  class="easyui-textbox"  data-options="required:true">
                </#if>
            </td>
        </tr>
        <#if actFormTpl??>
            <tr>
                <td>
                    版本:
                </td>
                <td>
                    ${(actFormTpl.formVersion)!}
                </td>
            </tr>
        </#if>
        <tr>
            <td>
                状态:
            </td>
            <td>
                <input name="state"  class="easyui-combobox"
               data-options="required:true,data: [{value:'1',text:'启用'} ,{value:'0',text:'禁用'}],
                         panelHeight:'auto',value:'${(actFormTpl.state)!}'">
            </td>
        </tr>
        <tr>
            <td>
                描述:
            </td>
            <td>
                <input name="remark" value="${(actFormTpl.remark)!}" class="easyui-textbox"   multiline="true"
                       style="height: 50px;width: 100%;">
            </td>
        </tr>
        <tr>
            <td>模板(html)：</td>
            <td>
                <input  name="file" class="easyui-filebox"  <#if !actFormTpl??>required="true"</#if>
                        data-options="buttonText: '选择文件', buttonAlign: 'right'">
                <#if actFormTpl??>
                    <div class="formWraning">
                        1. 替换模板可能对历史单据造成影响, 如不明确表单是否被使用、是否会造成异常，建议新增同Key模板，新流程使用会最新表单。
                        <br>
                        2. 如必须替换模板，请先将原始模板备档。
                    </div>
                </#if>
            </td>
        </tr>
        <tr>
            <td>
                插件:
            </td>
            <td>
                <input name="plugins" value="${(actFormTpl.plugins)!}" class="easyui-textbox"
                       multiline="true" style="width:100%;height: 50px;">
                <div class="formTip">现已支持:DatePicker、UploadFile, 多个插件之间用 英文逗号 分隔</div>
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

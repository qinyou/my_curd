<#-- chuang  2019-08-22 20:49:00 -->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if businessFormInfo?? >${ctx!}/businessFormInfo/updateAction<#else>${ctx!}/businessFormInfo/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="${(businessFormInfo.id)!}">
        <tbody>
        <tr>
            <td>
                分类:
            </td>
            <td>
                <input id="categoryId" name="categoryId"   >
            </td>
        </tr>
        <tr>
            <td>
                名字:
            </td>
            <td>
                <input name="name" value="${(businessFormInfo.name)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                图标:
            </td>
            <td>
                <input name="icon" value="${(businessFormInfo.icon)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                业务表:
            </td>
            <td>
                <input name="formName" value="${(businessFormInfo.formName)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                流程Key:
            </td>
            <td>
                <input name="processKey" value="${(businessFormInfo.processKey)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                描述信息:
            </td>
            <td>
                <input name="info" value="${(businessFormInfo.info)!}" class="easyui-textbox"
                       data-options="required:true,multiline:true" style="height: 50px; ">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script src="${ctx!}/static/js/input2combotree.js"></script>
<script>
    initFormCombotree('#categoryId','${(categoryId)!}','','${ctx!}/utils/orgComboTree?withRoot=false',true);
</script>
</@layout>

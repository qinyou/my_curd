<#--数据字典分组 表单-->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if sysDictGroup?? >${ctx!}/sysDict/updateGroupAction<#else>${ctx!}/sysDict/addGroupAction</#if>">
    <table class=" pure-table pure-table-horizontal centerTable labelInputTable">
        <input id="id" name="id" type="hidden" value="${(sysDictGroup.id)!}">
        <tbody>
        <tr>
            <td>名称：</td>
            <td>
                <input name="groupName" value="${(sysDictGroup.groupName)!}"   class="easyui-textbox"  data-options="required:true" >
            </td>
        </tr>
        <tr>
            <td>编码：</td>
            <td>
                <input name="groupCode" value="${(sysDictGroup.groupCode)!}"   class="easyui-textbox" data-options="required:true">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >
        <i class="iconfont icon-cancel"></i> 取消
    </button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" >
        <i class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

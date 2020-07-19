<#--数据字典表单-->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if sysDict?? >${ctx!}/sysDict/updateDictAction<#else>${ctx!}/sysDict/addDictAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
        <input id="id" name="id" type="hidden" value="${(sysDict.id)!}">
        <tbody>
        <tr>
            <td>分组：</td>
            <td>
                <input value="${(groupName)!}"   class="easyui-textbox"  data-options="required:true,readonly:true" >
                <input name="groupCode" type="hidden" value="${(groupCode)!}">
            </td>
        </tr>
        <tr>
            <td>状态：</td>
            <td>
                <input name="state"  class="easyui-combobox"  data-options=" data:[{value:'on',text:'启用'} ,{value:'off',text:'禁用'}],
                             editable: false,  required:true, panelHeight:'auto',  value:'${(sysDict.state)!'on'}' ">
            </td>
        </tr>
        <tr>
            <td>名称：</td>
            <td>
                <input name="dictLabel" value="${(sysDict.dictLabel)!}"   class="easyui-textbox"  data-options="required:true" >
            </td>
        </tr>
        <tr>
            <td>编码：</td>
            <td>
                <input name="dictValue" value="${(sysDict.dictValue)!}"   class="easyui-textbox" data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>排序号：</td>
            <td>
                <input name="sortNum" value="${(sysDict.sortNum)!}"   class="easyui-numberbox" data-options="required:true,precision:0,min:0">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name)" >
        <i class="iconfont icon-cancel"></i> 取消
    </button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg2')" >
        <i class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>

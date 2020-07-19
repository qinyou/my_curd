<#--角色表单-->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if sysRole?? >${ctx!}/sysRole/updateAction<#else>${ctx!}/sysRole/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
        <input id="id" name="id" type="hidden" value="${(sysRole.id)!}">
        <tbody>
        <tr>
            <td>名称：</td>
            <td>
                <input name="roleName" value="${(sysRole.roleName)!}"   class="easyui-textbox"  data-options="required:true"  style="width: 200px" >
            </td>
        </tr>
        <tr>
            <td>编码：</td>
            <td>
                <input name="roleCode" value="${(sysRole.roleCode)!}"   class="easyui-textbox" data-options="required:true"  style="width: 200px">
            </td>
        </tr>
        <tr>
            <td>排序号：</td>
            <td>
                <input name="sortNum" value="${(sysRole.sortNum)!}"   class="easyui-numberbox" data-options="required:true,precision:0,min:0"  style="width: 200px" >
            </td>
        </tr>
        <tr>
            <td>说明：</td>
            <td>
                <input name="roleDesc" value="${(sysRole.roleDesc)!}"   class="easyui-textbox" data-options="multiline:true"
                       style="width: 200px;height: 100px;">
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

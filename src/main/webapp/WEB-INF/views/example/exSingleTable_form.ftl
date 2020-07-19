<#-- zhangchuang  2019-02-22 21:43:17 -->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if exSingleTable?? >${ctx!}/exSingleTable/updateAction<#else>${ctx!}/exSingleTable/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="${(exSingleTable.id)!}">
        <tbody>
        <tr>
            <td>
                姓名:
            </td>
            <td>
                <input name="name" value="${(exSingleTable.name)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                年龄:
            </td>
            <td>
                <input name="age" value="${(exSingleTable.age)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                性别:
            </td>
            <td>
                <input name="gender" value="${(exSingleTable.gender)!}" class="easyui-textbox"  data-options="required:true">
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
</@layout>

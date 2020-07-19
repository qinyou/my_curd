<#-- zhangchuang  2019-06-15 20:24:55 -->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if sysSetting?? >${ctx!}/sysSetting/updateAction<#else>${ctx!}/sysSetting/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="${(sysSetting.id)!}">
        <tbody>
        <tr>
            <td>
                属性:
            </td>
            <td>
                <input name="settingCode" value="${(sysSetting.settingCode)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                属性值
            </td>
            <td>
                <input name="settingValue" value="${(sysSetting.settingValue)!}" class="easyui-textbox"   style="width: 100%;height: 150px;" data-options="required:true,multiline:true" >
            </td>
        </tr>
        <tr>
            <td>
                说明:
            </td>
            <td>
                <input name="settingInfo" value="${(sysSetting.settingInfo)!}" class="easyui-textbox"    style="width: 100%;height: 50px;"
                       data-options="required:true,multiline:true">
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

<#--机构 表单-->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if sysOrg?? >${ctx!}/sysOrg/updateAction<#else>${ctx!}/sysOrg/addAction</#if>">
    <table class=" pure-table pure-table-horizontal centerTable labelInputTable">
        <input id="id" name="id" type="hidden" value="${(sysOrg.id)!}">
        <tbody>
        <tr>
            <td>名称：</td>
            <td>
                <input name="orgName" value="${(sysOrg.orgName)!}"   class="easyui-textbox"  data-options="required:true" >
            </td>
            <td>编码：</td>
            <td>
                <input name="orgCode" value="${(sysOrg.orgCode)!}"   class="easyui-textbox" data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>父机构：</td>
            <td>
                <input name="pid"  id="pid">
            </td>
            <td>排序号：</td>
            <td>
                <input name="sortNum" value="${(sysOrg.sortNum)!}"   class="easyui-numberbox" data-options="required:true,precision:0,min:0">
            </td>

        </tr>
        <tr>
            <td>地址：</td>
            <td  colspan="3">
                <input name="address" value="${(sysOrg.address)!}"   class="easyui-textbox" data-options="required:true,multiline:true" style="height: 70px; width: 100%;">
            </td>
        </tr>
        <tr>
            <td>备注：</td>
            <td colspan="3">
                <input name="remark" value="${(sysOrg.remark)!}"   class="easyui-textbox" data-options="multiline:true" style="height: 70px; width: 100%;">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >
        <i class="iconfont icon-cancel"></i> 取消
    </button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','tg')" >
        <i class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="${ctx!}/static/js/tg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script src="${ctx!}/static/js/input2combotree.js"></script>
<script>
    initFormCombotree('#pid','${pid!}','${(sysOrg.id)!}','${ctx!}/utils/orgComboTree',true);
</script>
</@layout>
